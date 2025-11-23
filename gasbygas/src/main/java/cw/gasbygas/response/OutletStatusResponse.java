package cw.gasbygas.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OutletStatusResponse {
    private int outletId;
    private String district;
    private Map<String, Integer> stockLevels;
    private List<GasRequestResponse> pendingRequests;
}
