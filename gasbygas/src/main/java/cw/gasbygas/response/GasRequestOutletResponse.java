package cw.gasbygas.response;

import cw.gasbygas.domain.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GasRequestOutletResponse
{
    private int id;
    private int userId;
    private long orderNo;
    private int gasId;
    private int quantity;
    private RequestStatus status;
}
