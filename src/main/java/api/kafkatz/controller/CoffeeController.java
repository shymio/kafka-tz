package api.kafkatz.controller;

import api.kafkatz.entities.CoffeeStock;
import api.kafkatz.entities.RoastingRecord;
import api.kafkatz.repository.CoffeeStockRepository;
import api.kafkatz.repository.RoastingRecordRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coffee")
@Tag(name = "Coffee API", description = "API для управления запасами кофе и потерями обжарки")
public class CoffeeController {

    private final CoffeeStockRepository coffeeStockRepository;
    private final RoastingRecordRepository roastingRecordRepository;

    @GetMapping("/stocks")
    @Operation(
            summary = "Получить список остатков кофе",
            description = "Возвращает список остатков кофе с возможностью фильтрации по стране и сорту."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список остатков кофе получен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoffeeStock.class)))
    })
    public List<CoffeeStock> getStocks(
            @Parameter(description = "Страна происхождения кофе для фильтрации", example = "Brazil")
            @RequestParam(required = false) String country,

            @Parameter(description = "Сорт кофе для фильтрации", example = "Arabica")
            @RequestParam(required = false) String type) {

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

    @GetMapping("/losses/brigades")
    @Operation(
            summary = "Получить процент потерь по бригадам",
            description = "Возвращает средний процент потерь по каждой бригаде."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Процент потерь по бригадам получен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object[].class)))
    })
    public List<Object[]> getBrigadeLosses() {
        return roastingRecordRepository.findBrigadeLosses();
    }

    @GetMapping("/losses/countries")
    @Operation(
            summary = "Получить процент потерь по странам",
            description = "Возвращает средний процент потерь по каждой стране происхождения кофе."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Процент потерь по странам получен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object[].class)))
    })
    public List<Object[]> getCountryLosses() {
        return roastingRecordRepository.findCountryLosses();
    }

    @PostMapping("/stocks")
    @Operation(
            summary = "Добавить новый остаток кофе",
            description = "Создает новый остаток кофе в системе."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Остаток кофе создан",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoffeeStock.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<CoffeeStock> addStock(
            @Parameter(description = "Данные для нового остатка кофе")
            @RequestBody CoffeeStock coffeeStock) {

        CoffeeStock savedStock = coffeeStockRepository.save(coffeeStock);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStock);
    }

    @PutMapping("/stocks/{id}")
    @Operation(
            summary = "Обновить остаток кофе",
            description = "Обновляет информацию об остатке кофе по заданному ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Остаток кофе обновлен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoffeeStock.class))),
            @ApiResponse(responseCode = "404", description = "Остаток кофе не найден",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<CoffeeStock> updateStock(
            @Parameter(description = "ID остатка кофе для обновления", example = "1")
            @PathVariable Long id,

            @Parameter(description = "Новые данные для остатка кофе")
            @RequestBody CoffeeStock coffeeStock) {

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
    @Operation(
            summary = "Удалить остаток кофе",
            description = "Удаляет остаток кофе по заданному ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Остаток кофе удален"),
            @ApiResponse(responseCode = "404", description = "Остаток кофе не найден")
    })
    public ResponseEntity<Void> deleteStock(
            @Parameter(description = "ID остатка кофе для удаления", example = "1")
            @PathVariable Long id) {

        if (coffeeStockRepository.existsById(id)) {
            coffeeStockRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}

