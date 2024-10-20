package api.kafkatz.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoastingRecord {
    @Id
    private UUID brigadeId;
    private String country;
    private String type;
    private int inputWeight;
    private int outputWeight;
    private double lossPercent;
}
