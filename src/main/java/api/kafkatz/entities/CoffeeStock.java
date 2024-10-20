package api.kafkatz.entities;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "coffee_stock")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private int robustaPercent;
    private int arabicaPercent;
    private String type;
    private double weightInGrams;


}
