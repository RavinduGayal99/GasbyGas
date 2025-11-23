package cw.gasbygas.service.impl;

import cw.gasbygas.domain.*;
import cw.gasbygas.exceptionHandling.InsufficientStockException;
import cw.gasbygas.exceptionHandling.ResourceNotFoundException;
import cw.gasbygas.model.*;
import cw.gasbygas.repository.*;
import cw.gasbygas.request.BulkGasRequest;
import cw.gasbygas.request.GasRequestDTO;
import cw.gasbygas.request.GasRequestOutletRequest;
import cw.gasbygas.response.GasRequestOutletResponse;
import cw.gasbygas.response.GasRequestResponse;
import cw.gasbygas.service.GasRequestService;
import cw.gasbygas.service.TokenService;
import cw.gasbygas.util.EmailService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.sql.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GasRequestServiceImpl implements GasRequestService {
    private final GasRequestRepository gasRequestRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final TokenService tokenService;
    private final GasRepository gasRepository;
    private final OutletRepository outletRepository;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public GasRequestResponse createDomesticRequest(GasRequestDTO request) {
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getType() != UserType.CUSTOMER) {
            throw new IllegalArgumentException("Invalid user type for domestic request");
        }

        var gas = gasRepository.findById(request.getGasId())
                .orElseThrow(() -> new ResourceNotFoundException("Gas not found"));

        var outlet = outletRepository.findById(request.getOutletId())
                .orElseThrow(() -> new ResourceNotFoundException("Outlet not found"));

        validateStockAvailability(request.getOutletId(), request.getGasId(), request.getQuantity());

        var pickupDate = calculatePickupDate();
        var token = tokenService.generateToken(pickupDate);

        var gasRequest = new GasRequest();
        gasRequest.setUser(user);
        gasRequest.setGas(gas);
        gasRequest.setOutlet(outlet);
        gasRequest.setToken(token);
        gasRequest.setUnitPrice(request.getUnitPrice());
        gasRequest.setQuantity(request.getQuantity());
        gasRequest.setTotalPrice(request.getTotalPrice());
        gasRequest.setStatus(RequestStatus.PENDING_VERIFICATION);

        gasRequest = gasRequestRepository.save(gasRequest);

        emailService.sendDomesticCustomerRequestEmail(gasRequest.getUser().getEmail(), token, gasRequest);

        return mapToResponse(gasRequest, token);
    }

    @Override
    @Transactional
    public GasRequestResponse createBusinessRequest(GasRequestDTO request) {
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getType() != UserType.CUSTOMER) {
            throw new IllegalArgumentException("Invalid user type for business request");
        }

        validateStockAvailability(request.getOutletId(), request.getGasId(), request.getQuantity());

        var gasRequest = new GasRequest();
        BeanUtils.copyProperties(request, gasRequest);
        gasRequest.setStatus(RequestStatus.PENDING_VERIFICATION);

        gasRequest = gasRequestRepository.save(gasRequest);

        emailService.sendBusinessCustomerRequestEmail(user.getEmail(), gasRequest);

        return mapToResponse(gasRequest);
    }

    @Override
    @Transactional
    public List<GasRequestResponse> createBulkOutletRequest(BulkGasRequest request) {
        // Validate all stock availability first
        request.getRequests().forEach(req ->
                validateStockAvailability(req.getOutletId(), req.getGasId(), req.getQuantity())
        );

        return request.getRequests().stream()
                .map(req -> {
                    var gasRequest = new GasRequest();
                    BeanUtils.copyProperties(req, gasRequest);
                    gasRequest.setStatus(RequestStatus.PENDING_VERIFICATION);
                    return gasRequestRepository.save(gasRequest);
                })
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<GasRequestOutletResponse> createOutletRequest(List<GasRequestOutletRequest> requests)
    {
        if (requests.isEmpty()) {
            throw new IllegalArgumentException("Request list cannot be empty");
        }

        User user = userRepository.findById(requests.get(0).getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String timestamp = DateTimeFormatter.ofPattern("yyMMddHHmmss").format(LocalDateTime.now());
        long orderNo = Long.parseLong(user.getId() + timestamp);

        List<GasRequest> savedRequests = requests.stream().map(request -> {
            Gas gas = gasRepository.findById(request.getGasId())
                    .orElseThrow(() -> new IllegalArgumentException("Gas not found"));

            GasRequest gasRequest = new GasRequest();
            gasRequest.setUser(user);
            gasRequest.setGas(gas);
            gasRequest.setOrderNo(orderNo);
            gasRequest.setQuantity(request.getQuantity());
            gasRequest.setStatus(RequestStatus.PENDING_VERIFICATION);

            return gasRequestRepository.save(gasRequest);
        }).toList();

        return savedRequests.stream().map(gr -> new GasRequestOutletResponse(
                gr.getId(),
                gr.getUser().getId(),
                gr.getOrderNo(),
                gr.getGas().getId(),
                gr.getQuantity(),
                gr.getStatus()
        )).collect(Collectors.toList());
    }

    @Override
    public List<GasRequestResponse> getRequestsByUserAndStatus(int userId, RequestStatus status) {
        return gasRequestRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<GasRequestResponse> getRequestsByOutletAndStatus(int outletId, RequestStatus status) {
        return gasRequestRepository.findByOutletIdAndStatus(outletId, status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void updateRequestStatus(int requestId, RequestStatus status) {
//todo
    }

    @Transactional
    @Override
    public void updateRequestStatus(long orderNo, RequestStatus status) {
        var requests = gasRequestRepository.findByOrderNo(orderNo);
        if (requests.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Gas requests not found for order: %s", orderNo));
        }

        requests.forEach(request -> {
            request.setStatus(status);
            if (status == RequestStatus.REJECTED && request.getToken() != null) {
                tokenService.updateTokenStatus(request.getToken().getId(), TokenStatus.UNASSIGNED);
            }
        });
    }

    @Override
    public List<GasRequestResponse> getDomesticRequests(String statusFilter) {
        List<RequestStatus> statuses;

        switch (statusFilter.toUpperCase()) {
            case "PENDING_VERIFICATION" -> statuses = List.of(RequestStatus.PENDING_VERIFICATION, RequestStatus.VERIFIED, RequestStatus.PAID);
            case "COMPLETED" -> statuses = List.of(RequestStatus.COMPLETED, RequestStatus.REALLOCATED, RequestStatus.REJECTED);
            default -> throw new IllegalArgumentException("Invalid status filter");
        }

        return gasRequestRepository.findDomesticRequestsByStatuses(statuses)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void cancelDomesticRequest(int requestId, int userId) {
        GasRequest request = gasRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Gas request not found"));

        if (request.getUser().getId() != (userId)) {
            throw new IllegalStateException("Unauthorized action");
        }

        if (request.getStatus() != RequestStatus.PENDING_VERIFICATION && request.getStatus() != RequestStatus.VERIFIED) {
            throw new IllegalStateException("Request cannot be canceled at this stage");
        }

        request.setStatus(RequestStatus.REJECTED);
        gasRequestRepository.save(request);

        if (request.getToken() != null) {
            tokenService.updateTokenStatus(request.getToken().getId(), TokenStatus.UNASSIGNED);
        }
    }

    @Override
    public List<GasRequestResponse> getOutletRequestsByStatus(int outletId, RequestStatus status) {
        return gasRequestRepository.findOutletRequestsByStatus(outletId, status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void updateOutletRequestStatus(int requestId, RequestStatus status) {
        GasRequest request = gasRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Gas request not found"));

        if (request.getOutlet() == null) {
            throw new IllegalStateException("The request must belong to an outlet");
        }

        request.setStatus(status);
        gasRequestRepository.save(request);
    }

    @Override
    public List<GasRequestResponse> getRequestsByStatusForOutlet(int outletId, String statusFilter) {
        List<RequestStatus> statuses;

        switch (statusFilter.toUpperCase()) {
            case "PENDING":
                statuses = List.of(RequestStatus.PENDING_VERIFICATION, RequestStatus.VERIFIED, RequestStatus.PAID);
                break;
            case "FINALIZED":
                statuses = List.of(RequestStatus.COMPLETED, RequestStatus.REALLOCATED, RequestStatus.REJECTED);
                break;
            default:
                throw new IllegalArgumentException("Invalid status filter");
        }

        return gasRequestRepository.findByOutletIdAndStatusIn(outletId, statuses)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<Gas> getAllGasItems() {
        return gasRepository.findAll();
    }

    @Override
    public Optional<Gas> getGasById(Integer id) {
        return gasRepository.findById(id);
    }

    @Override
    public List<Gas> getGasByIds(List<Integer> ids) {
        return gasRepository.findAllById(ids);
    }

    @Override
    public List<Outlet> getVerifiedOutlets() {
        return outletRepository.findByUserStatus(UserStatus.VERIFIED);
    }

    @Override
    public List<Outlet> getVerifiedRetailAndAllOutlets() {
        return outletRepository.findByTypeInAndUser_Status(
                Arrays.asList(OutletType.RETAIL, OutletType.ALL),
                UserStatus.VERIFIED
        );
    }

    @Override
    public List<Outlet> getVerifiedBulkOutlets() {
        return outletRepository.findByTypeInAndUser_Status(
                Collections.singletonList(OutletType.BULK),
                UserStatus.VERIFIED
        );
    }

    @Override
    public List<Outlet> getVerifiedAllOutlets() {
        return outletRepository.findByTypeAndUser_Status(
                OutletType.ALL,
                UserStatus.VERIFIED
        );
    }

    @Override
    public List<Customer> getVerifiedBusiness() {
        return customerRepository.findByUserStatusAndType(UserStatus.VERIFIED, CustomerType.BUSINESS);
    }

    @Override
    public void cancelBusinessRequest(int requestId) {
        GasRequest request = gasRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        request.setStatus(RequestStatus.REJECTED);
        gasRequestRepository.save(request);
    }

    private void validateStockAvailability(int outletId, int gasId, int requestedQuantity) {
        var stock = stockRepository.findByOutletIdAndGasId(outletId, gasId)
                .orElseThrow(() -> new ResourceNotFoundException("Gas stock not found"));

        if (stock.getQuantity() < requestedQuantity) {
            throw new InsufficientStockException(
                    String.format("Insufficient stock available. Requested: %d, Available: %d", requestedQuantity, stock.getQuantity())
            );
        }
    }

    private GasRequestResponse mapToResponse(GasRequest gasRequest) {
        return GasRequestResponse.builder()
                .id(gasRequest.getId())
                .orderNo(gasRequest.getOrderNo())
                .userId(gasRequest.getUser().getId())
                .gasId(gasRequest.getGas().getId())
                .outletId(gasRequest.getOutlet().getId())
                .unitPrice(gasRequest.getUnitPrice())
                .quantity(gasRequest.getQuantity())
                .totalPrice(gasRequest.getTotalPrice())
                .status(gasRequest.getStatus())
                .build();
    }

    private GasRequestResponse mapToResponse(GasRequest gasRequest, Token token) {
        return GasRequestResponse.builder()
                .id(gasRequest.getId())
                .userId(gasRequest.getUser().getId())
                .gasId(gasRequest.getGas().getId())
                .outletId(gasRequest.getOutlet().getId())
                .unitPrice(gasRequest.getUnitPrice())
                .quantity(gasRequest.getQuantity())
                .totalPrice(gasRequest.getTotalPrice())
                .status(gasRequest.getStatus())
                .tokenId(token != null ? token.getId() : null)
                .pickupDate(token != null ? token.getPickupDate() : null)
                .build();
    }

    private Date calculatePickupDate() {
        LocalDateTime pickupDateTime = LocalDateTime.now().plusWeeks(1);
        return Date.valueOf(pickupDateTime.toLocalDate());
    }

    private static final Map<String, List<RequestStatus>> STATUS_FILTER_MAP = Map.of(
            "Pending", List.of(RequestStatus.PENDING_VERIFICATION, RequestStatus.VERIFIED, RequestStatus.PAID),
            "Finalized", List.of(RequestStatus.COMPLETED, RequestStatus.REALLOCATED, RequestStatus.REJECTED)
    );
}
