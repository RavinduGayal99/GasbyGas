package cw.gasbygas.response;

import cw.gasbygas.domain.RequestStatus;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class GasRequestResponse {
    private int id;
    private int userId;
    private long orderNo;
    private int tokenId;
    private int gasId;
    private int outletId;
    private Date pickupDate;
    private double unitPrice;
    private int quantity;
    private double totalPrice;
    private RequestStatus status;
}
