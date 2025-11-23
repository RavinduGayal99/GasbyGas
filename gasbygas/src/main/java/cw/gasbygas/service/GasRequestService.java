package cw.gasbygas.service;

import cw.gasbygas.domain.RequestStatus;
import cw.gasbygas.model.Customer;
import cw.gasbygas.model.Gas;
import cw.gasbygas.model.Outlet;
import cw.gasbygas.request.BulkGasRequest;
import cw.gasbygas.request.GasRequestDTO;
import cw.gasbygas.request.GasRequestOutletRequest;
import cw.gasbygas.response.GasRequestOutletResponse;
import cw.gasbygas.response.GasRequestResponse;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface GasRequestService {
    GasRequestResponse createDomesticRequest(GasRequestDTO request);
    GasRequestResponse createBusinessRequest(GasRequestDTO request);
    List<GasRequestResponse> createBulkOutletRequest(BulkGasRequest request);
    List<GasRequestResponse> getRequestsByUserAndStatus(int userId, RequestStatus status);
    List<GasRequestResponse> getRequestsByOutletAndStatus(int outletId, RequestStatus status);
    void updateRequestStatus(int requestId, RequestStatus status);

    @Transactional
    void updateRequestStatus(long orderNo, RequestStatus status);

    List<GasRequestResponse> getDomesticRequests(String statusFilter);

    void cancelDomesticRequest(int requestId, int userId);

    List<GasRequestResponse> getOutletRequestsByStatus(int outletId, RequestStatus status);

    void updateOutletRequestStatus(int requestId, RequestStatus status);

    List<GasRequestResponse> getRequestsByStatusForOutlet(int outletId, String statusFilter);

    List<Gas> getAllGasItems();
    Optional<Gas> getGasById(Integer id);
    List<Gas> getGasByIds(List<Integer> ids);

    List<Outlet> getVerifiedOutlets();
    List<Customer> getVerifiedBusiness();
    void cancelBusinessRequest(int requestId);

    List<Outlet> getVerifiedRetailAndAllOutlets();
    List<Outlet> getVerifiedBulkOutlets();
    List<Outlet> getVerifiedAllOutlets();

    List<GasRequestOutletResponse> createOutletRequest(List<GasRequestOutletRequest> request);
}
