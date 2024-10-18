package api.kafkatz.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CoffeeBeanMessage {
    private String country;
    private int robustaPercent;
    private int arabicaPercent;
    private String type;
    private double weightInGrams;
}
