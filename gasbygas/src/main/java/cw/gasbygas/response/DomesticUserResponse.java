package cw.gasbygas.response;

import cw.gasbygas.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DomesticUserResponse
{
    private int id;
    private String name;
    private String email;
    private String contact;
    private UserStatus status;
}
