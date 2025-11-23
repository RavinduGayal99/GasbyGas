package cw.gasbygas.request;

import lombok.Data;

@Data
public class CustomerRegistrationRequest {
    private String email;
    private String password;
    private String name;
    private String contact;
    private String nic;
}
