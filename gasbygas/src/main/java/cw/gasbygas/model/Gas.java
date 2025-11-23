package cw.gasbygas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gas")
public class Gas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String item;
    private double price;
}
