package cw.gasbygas.model;

import cw.gasbygas.domain.RequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request")
public class GasRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "gasId")
    private Gas gas;

    @ManyToOne
    @JoinColumn(name = "outletId")
    private Outlet outlet;

    @OneToOne
    @JoinColumn(name = "tokenId")
    private Token token;

    private long orderNo;
    private double unitPrice;
    private int quantity;
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}