package com.vcc.bigdata.stock.application.port.out;

import com.vcc.bigdata.stock.domain.model.identity.Stock;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public interface StockCachePort {
    void saveAll(Collection<Stock> stocks);

    void save(Stock stock);

    Optional<Stock> findBySymbol(String symbol);

    Map<String, Stock> findBySymbol(Collection<String> symbols);

    void cacheNullSymbol(String symbol);

    void deleteBySymbol(String symbol);

    Map<String, String> getAllSymbolHashes();

    void saveSymbolHashes(Map<String, String> symbolHashes);
}
