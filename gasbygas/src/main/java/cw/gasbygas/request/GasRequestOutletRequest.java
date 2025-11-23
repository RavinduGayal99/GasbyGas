package cw.gasbygas.request;

import lombok.Data;

@Data
public class GasRequestOutletRequest
{
    private int userId;
    private int gasId;
    private int quantity;
}
