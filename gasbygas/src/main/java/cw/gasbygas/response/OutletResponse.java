package cw.gasbygas.response;

import cw.gasbygas.domain.OutletType;
import lombok.Data;

@Data
public class OutletResponse {
    private int id;
    private String name;
    private String district;
    private String type;

    public OutletResponse(int id, String name, String district, String type) {
        this.id = id;
        this.name = name;
        this.district = district;
        this.type = type;
    }
}