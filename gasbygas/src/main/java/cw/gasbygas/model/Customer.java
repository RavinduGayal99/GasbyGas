package cw.gasbygas.model;

import cw.gasbygas.domain.CustomerGasType;
import cw.gasbygas.domain.CustomerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "UserId", referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    private CustomerType type;

    @Enumerated(EnumType.STRING)
    private CustomerGasType gasType;
}