package api.kafkatz.service;

import api.kafkatz.entities.CoffeeStock;
import api.kafkatz.model.CoffeeBeanMessage;
import api.kafkatz.repository.CoffeeStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final CoffeeStockRepository coffeeStockRepository;

    @KafkaListener(topics = "coffee_beans", groupId = "coffee_group")
    public void consume(CoffeeBeanMessage message) {
        CoffeeStock coffeeStock = CoffeeStock.builder()
                .country(message.getCountry())
                .type(message.getType())
                .robustaPercent(message.getRobustaPercent())
                .arabicaPercent(message.getArabicaPercent())
                .weightInGrams(message.getWeightInGrams())
                .build();

        coffeeStockRepository.save(coffeeStock);
    }

}
