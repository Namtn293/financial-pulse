package com.vcc.bigdata.stock.application.port.out;

import java.util.List;

public interface StockKafkaPort {
    void publishUpdatedStock(List<String> symbols);
}
