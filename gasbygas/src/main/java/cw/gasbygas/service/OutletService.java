package cw.gasbygas.service;

import cw.gasbygas.model.Stock;
import cw.gasbygas.response.OutletResponse;
import cw.gasbygas.response.OutletStatusResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OutletService {
    List<OutletResponse> getOutletsByDistrict(String district);
    //OutletStatusResponse getOutletStatus(int outletId);
    List<Stock> getAllStockForOutlet(int outletId);
    void updateStock(int outletId, int gasId, int quantity);
}
