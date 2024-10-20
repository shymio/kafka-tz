package api.kafkatz.controller;

import api.kafkatz.entities.CoffeeStock;
import api.kafkatz.entities.RoastingRecord;
import api.kafkatz.repository.CoffeeStockRepository;
import api.kafkatz.repository.RoastingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coffee")
public class CoffeeController {

    private final CoffeeStockRepository coffeeStockRepository;
    private final RoastingRecordRepository roastingRecordRepository;

    @GetMapping("/stocks")
    public List<CoffeeStock> getStocks(@RequestParam(required = false) String country,
                                       @RequestParam(required = false) String type) {
        // Логика фильтрации по стране и сорту
        if (country != null && type != null) {
            return coffeeStockRepository.findByCountryAndType(country, type);
        } else if (country != null) {
            return coffeeStockRepository.findByCountry(country);
        } else if (type != null) {
            return coffeeStockRepository.findByType(type);
        } else {
            return coffeeStockRepository.findAll();
        }
    }

    @GetMapping("/losses")
    public List<RoastingRecord> getBrigadeLosses() {
        return roastingRecordRepository.findAll();
    }

    @PostMapping("/stocks")
    public ResponseEntity<CoffeeStock> addStock(@RequestBody CoffeeStock coffeeStock) {
        CoffeeStock savedStock = coffeeStockRepository.save(coffeeStock);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStock);
    }

    @PutMapping("/stocks/{id}")
    public ResponseEntity<CoffeeStock> updateStock(@PathVariable Long id, @RequestBody CoffeeStock coffeeStock) {
        Optional<CoffeeStock> stockOptional = coffeeStockRepository.findById(id);

        if (stockOptional.isPresent()) {
            CoffeeStock existingStock = stockOptional.get();
            existingStock.setCountry(coffeeStock.getCountry());
            existingStock.setType(coffeeStock.getType());
            coffeeStockRepository.save(existingStock);
            return ResponseEntity.ok(existingStock);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        if (coffeeStockRepository.existsById(id)) {
            coffeeStockRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
