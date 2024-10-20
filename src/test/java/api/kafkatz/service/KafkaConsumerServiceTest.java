package api.kafkatz.service;

import api.kafkatz.entities.CoffeeStock;
import api.kafkatz.model.CoffeeBeanMessage;
import api.kafkatz.repository.CoffeeStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class KafkaConsumerServiceTest {

    @Mock
    private CoffeeStockRepository coffeeStockRepository;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsume() {
        // Arrange
        CoffeeBeanMessage message = new CoffeeBeanMessage();
        message.setCountry("Brazil");
        message.setType("Arabica");
        message.setRobustaPercent(20);
        message.setArabicaPercent(80);
        message.setWeightInGrams(1000);

        // Act
        kafkaConsumerService.consume(message);

        // Assert
        ArgumentCaptor<CoffeeStock> coffeeStockCaptor = ArgumentCaptor.forClass(CoffeeStock.class);
        verify(coffeeStockRepository).save(coffeeStockCaptor.capture());

        CoffeeStock savedCoffeeStock = coffeeStockCaptor.getValue();
        assertEquals("Brazil", savedCoffeeStock.getCountry());
        assertEquals("Arabica", savedCoffeeStock.getType());
        assertEquals(20, savedCoffeeStock.getRobustaPercent());
        assertEquals(80, savedCoffeeStock.getArabicaPercent());
        assertEquals(1000, savedCoffeeStock.getWeightInGrams());
    }
}
