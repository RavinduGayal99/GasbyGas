package cw.gasbygas.model;

import cw.gasbygas.domain.TokenStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "pickupDate")
    private Date pickupDate;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;
}
