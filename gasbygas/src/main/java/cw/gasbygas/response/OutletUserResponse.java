package cw.gasbygas.response;

import cw.gasbygas.domain.OutletType;
import cw.gasbygas.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutletUserResponse {
    private int id;
    private String name;
    private String email;
    private String contact;
    private OutletType type;
    private String district;
    private UserStatus status;
}
