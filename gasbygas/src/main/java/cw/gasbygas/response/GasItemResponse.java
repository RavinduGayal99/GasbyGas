package cw.gasbygas.response;

import lombok.Data;

@Data
public class GasItemResponse {
    private int id;
    private String item;
    private double price;
}
