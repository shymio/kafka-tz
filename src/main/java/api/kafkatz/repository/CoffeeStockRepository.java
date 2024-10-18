package api.kafkatz.repository;

import api.kafkatz.entities.CoffeeStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoffeeStockRepository extends JpaRepository<CoffeeStock, Long> {

    List<CoffeeStock> findByCountry(String country);

    List<CoffeeStock> findByType(String type);

    List<CoffeeStock> findByCountryAndType(String country, String type);

}
