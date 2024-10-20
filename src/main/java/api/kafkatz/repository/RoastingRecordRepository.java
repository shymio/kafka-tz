package api.kafkatz.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import api.kafkatz.entities.RoastingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoastingRecordRepository extends JpaRepository<RoastingRecord, UUID> {

    // Потери по бригадам
    @Query("SELECT r.brigadeId, AVG(r.lossPercent) AS avgLossPercent FROM RoastingRecord r GROUP BY r.brigadeId")
    List<Object[]> findBrigadeLosses();

    // Потери по странам
    @Query("SELECT r.country, AVG(r.lossPercent) AS avgLossPercent FROM RoastingRecord r GROUP BY r.country")
    List<Object[]> findCountryLosses();

}
