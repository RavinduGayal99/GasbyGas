package cw.gasbygas.request;

import cw.gasbygas.domain.OutletType;
import lombok.Data;

@Data
public class OutletRegistrationRequest {
    private String email;
    private String password;
    private String name;
    private String contact;
    private String district;
    private OutletType outletType;
}