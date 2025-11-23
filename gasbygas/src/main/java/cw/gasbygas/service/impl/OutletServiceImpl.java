package cw.gasbygas.service.impl;

import cw.gasbygas.domain.RequestStatus;
import cw.gasbygas.model.Outlet;
import cw.gasbygas.model.Stock;
import cw.gasbygas.repository.GasRequestRepository;
import cw.gasbygas.repository.OutletRepository;
import cw.gasbygas.repository.StockRepository;
import cw.gasbygas.response.GasRequestResponse;
import cw.gasbygas.response.OutletResponse;
import cw.gasbygas.response.OutletStatusResponse;
import cw.gasbygas.service.OutletService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OutletServiceImpl implements OutletService {

    private final OutletRepository outletRepository;
    private final StockRepository stockRepository;
    private final GasRequestRepository gasRequestRepository;

    public OutletServiceImpl(OutletRepository outletRepository,
                             StockRepository stockRepository,
                             GasRequestRepository gasRequestRepository) {
        this.outletRepository = outletRepository;
        this.stockRepository = stockRepository;
        this.gasRequestRepository = gasRequestRepository;
    }

    @Override
    public List<OutletResponse> getOutletsByDistrict(String district) {
        List<Outlet> outlets;
        if (district != null && !district.isEmpty()) {
            outlets = outletRepository.findByDistrict(district);
        } else {
            outlets = outletRepository.findAll();
        }
        // Convert Outlet objects to OutletResponse DTOs
        return outlets.stream()
                .map(outlet -> new OutletResponse(outlet.getId(), outlet.getUser().getName(), outlet.getDistrict(), outlet.getType().name()))
                .collect(Collectors.toList());
    }

    /*@Override
    public OutletStatusResponse getOutletStatus(int outletId) {
        Outlet outlet = outletRepository.findById(outletId)
                .orElseThrow(() -> new RuntimeException("Outlet not found"));

        Map<String, Integer> stockLevels = stockRepository.findByOutletId(outletId).stream()
                .collect(Collectors.toMap(
                        stock -> stock.getGas().getItem(),
                        Stock::getQuantity
                ));

        List<GasRequestResponse> pendingRequests = gasRequestRepository.findOutletRequestsByStatus(outletId, RequestStatus.PENDING_VERIFICATION)
                .stream()
                .map(request -> new GasRequestResponse(
                        request.getId(),
                        request.getOrderNo(),
                        request.getQuantity(),
                        request.getCreatedAt()
                ))
                .collect(Collectors.toList());

        OutletStatusResponse response = new OutletStatusResponse();
        response.setOutletId(outlet.getId());
        response.setDistrict(outlet.getDistrict());
        response.setStockLevels(stockLevels);
        response.setPendingRequests(pendingRequests);

        return response;
    }*/

    @Override
    public List<Stock> getAllStockForOutlet(int outletId)
    {
        return stockRepository.findByOutletId(outletId);
    }

    @Override
    @Transactional
    public void updateStock(int outletId, int gasId, int quantity) {
        int updatedRows = stockRepository.updateStock(outletId, gasId, quantity);
        if (updatedRows == 0) {
            throw new EntityNotFoundException("Stock record not found for outletId: " + outletId + " and gasId: " + gasId);
        }
    }
}
