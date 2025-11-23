package cw.gasbygas.request;

import lombok.Data;

import java.util.List;

@Data
public class BulkGasRequest {
    private int userId;
    private List<GasRequestDTO> requests;
}
