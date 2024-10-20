package api.kafkatz.repository;

import api.kafkatz.entities.RoastingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoastingRecordRepository extends JpaRepository<RoastingRecord, UUID> {
}
