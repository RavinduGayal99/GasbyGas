package cw.gasbygas.response;

import cw.gasbygas.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private int id;
    private String name;
    private String email;
    private UserStatus status;
}
