package com.vcc.bigdata.stock.application.port.out;

import com.vcc.bigdata.stock.domain.model.identity.Stock;

import java.util.List;
import java.util.Map;

public interface StockRepositoryPort {
    int saveAll(List<Stock> stocks);

    int saveAllStockSsi(List<Stock> stocks);

    Map<String, String> getAllSymbolHashes();
}
