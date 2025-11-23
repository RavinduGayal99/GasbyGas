package cw.gasbygas.request;

import cw.gasbygas.domain.CustomerGasType;
import lombok.Data;

@Data
public class BusinessRegistrationRequest {
    private String email;
    private String password;
    private String name;
    private String contact;
    private CustomerGasType gasType;
}
