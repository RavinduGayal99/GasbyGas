package cw.gasbygas.model;

import cw.gasbygas.domain.UserStatus;
import cw.gasbygas.domain.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "user")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private UserType type;
    private String email;
    private String password;

    @Size(min = 9, max = 10)
    private String contact;

    @Column(nullable = true)
    private String nic;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
