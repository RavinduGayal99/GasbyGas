package cw.gasbygas.request;

import lombok.Data;

@Data
public class GasRequestDTO {
    private int userId;
    private int gasId;
    private int outletId;
    private double unitPrice;
    private int quantity;
    private double totalPrice;
}