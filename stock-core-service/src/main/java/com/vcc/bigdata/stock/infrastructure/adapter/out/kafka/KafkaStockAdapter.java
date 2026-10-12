package com.vcc.bigdata.stock.infrastructure.adapter.out.kafka;

import com.vcc.bigdata.stock.application.port.out.StockKafkaPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaStockAdapter implements StockKafkaPort {
    @Override
    public void publishUpdatedStock(List<String> symbols) {
        if (symbols == null || symbols.isEmpty()){
            throw new IllegalArgumentException("Symbols partition key must not be null or empty!");
        }

    }
}
