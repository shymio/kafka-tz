package api.kafkatz.controller;

import api.kafkatz.entities.CoffeeStock;
import api.kafkatz.repository.CoffeeStockRepository;
import api.kafkatz.repository.RoastingRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CoffeeControllerTest {

    @Mock
    private CoffeeStockRepository coffeeStockRepository;

    @Mock
    private RoastingRecordRepository roastingRecordRepository;

    @InjectMocks
    private CoffeeController coffeeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStocks() throws Exception {

        CoffeeStock stock = CoffeeStock.builder()
                .country("Brazil")
                .robustaPercent(20) // Добавляем процент робусты
                .arabicaPercent(80) // Добавляем процент арабики
                .type("Arabica")
                .weightInGrams(1000) // Указываем вес в граммах
                .build();

        List<CoffeeStock> stocks = Collections.singletonList(stock);

        when(coffeeStockRepository.findAll()).thenReturn(stocks); // Настройка поведения мока

        List<CoffeeStock> result = coffeeController.getStocks(null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCountry()).isEqualTo("Brazil");
        assertThat(result.get(0).getType()).isEqualTo("Arabica");
        assertThat(result.get(0).getRobustaPercent()).isEqualTo(20); // Проверка процента робусты
        assertThat(result.get(0).getArabicaPercent()).isEqualTo(80); // Проверка процента арабики
        assertThat(result.get(0).getWeightInGrams()).isEqualTo(1000); // Проверка веса в граммах
    }

    @Test
    public void testAddStock() {
        CoffeeStock stock = CoffeeStock.builder()
                .country("Brazil")
                .robustaPercent(20)
                .arabicaPercent(80)
                .type("Arabica")
                .weightInGrams(1000)
                .build();

        when(coffeeStockRepository.save(any(CoffeeStock.class))).thenReturn(stock);

        ResponseEntity<CoffeeStock> response = coffeeController.addStock(stock);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(stock);
    }

    @Test
    public void testUpdateStock() {
        CoffeeStock existingStock = CoffeeStock.builder()
                .id(1L)
                .country("Brazil")
                .robustaPercent(20)
                .arabicaPercent(80)
                .type("Arabica")
                .weightInGrams(1000)
                .build();

        when(coffeeStockRepository.findById(1L)).thenReturn(Optional.of(existingStock));
        when(coffeeStockRepository.save(any(CoffeeStock.class))).thenReturn(existingStock);

        CoffeeStock updatedStock = CoffeeStock.builder()
                .country("Colombia")
                .robustaPercent(10)
                .arabicaPercent(90)
                .type("Arabica")
                .weightInGrams(1500)
                .build();

        ResponseEntity<CoffeeStock> response = coffeeController.updateStock(1L, updatedStock);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getCountry()).isEqualTo("Colombia");
        assertThat(response.getBody().getRobustaPercent()).isEqualTo(10);
        assertThat(response.getBody().getWeightInGrams()).isEqualTo(1500);
    }

    @Test
    public void testUpdateStockNotFound() {
        CoffeeStock updatedStock = CoffeeStock.builder()
                .country("Colombia")
                .robustaPercent(10)
                .arabicaPercent(90)
                .type("Arabica")
                .weightInGrams(1500)
                .build();

        when(coffeeStockRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<CoffeeStock> response = coffeeController.updateStock(1L, updatedStock);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteStock() {
        when(coffeeStockRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<Void> response = coffeeController.deleteStock(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(coffeeStockRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteStockNotFound() {
        when(coffeeStockRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<Void> response = coffeeController.deleteStock(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(coffeeStockRepository, never()).deleteById(any(Long.class)); // Убедитесь, что deleteById не вызывался
    }

    @Test
    public void testGetBrigadeLosses() {
        // Ваш код для тестирования метода getBrigadeLosses
        // Здесь предполагается, что метод возвращает List<Object[]>
        when(roastingRecordRepository.findBrigadeLosses()).thenReturn(Collections.emptyList());

        List<Object[]> losses = coffeeController.getBrigadeLosses();

        assertThat(losses).isEmpty();
    }

    @Test
    public void testGetCountryLosses() {
        // Ваш код для тестирования метода getCountryLosses
        when(roastingRecordRepository.findCountryLosses()).thenReturn(Collections.emptyList());

        List<Object[]> losses = coffeeController.getCountryLosses();

        assertThat(losses).isEmpty();
    }
}
