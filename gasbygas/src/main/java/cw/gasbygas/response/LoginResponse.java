package cw.gasbygas.response;

import cw.gasbygas.domain.CustomerType;
import cw.gasbygas.domain.UserType;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private UserType userType;
    private String name;
    private CustomerType customerType;
    private int userId;

    public LoginResponse(String token, UserType type, String name, CustomerType customerType, int userId) {
        this.token = token;
        this.userType = type;
        this.name = name;
        this.customerType = customerType;
        this.userId = userId;
    }
}
