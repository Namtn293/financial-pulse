package com.vcc.bigdata.stock.application.service;

import com.vcc.bigdata.stock.application.dto.SyncStockResult;
import com.vcc.bigdata.stock.application.port.in.StockUseCase;
import com.vcc.bigdata.stock.application.port.out.SsiStockPort;
import com.vcc.bigdata.stock.application.port.out.StockCachePort;
import com.vcc.bigdata.stock.application.port.out.StockKafkaPort;
import com.vcc.bigdata.stock.application.port.out.StockRepositoryPort;
import com.vcc.bigdata.stock.domain.model.identity.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService implements StockUseCase {

    private final SsiStockPort ssiStockPort;
    private final StockCachePort stockCachePort;
    private final StockRepositoryPort stockRepositoryPort;
    private final StockKafkaPort stockKafkaPort;

    @Override
    public SyncStockResult sync() {
        List<Stock> stocks = ssiStockPort.fetchSymbolInfo();
        if (stocks == null || stocks.isEmpty()) {
            return new SyncStockResult(0, 0, List.of());
        }

        Map<String, String> existingHashes = getExistingHashes();
        List<Stock> changedStocks = new ArrayList<>();
        List<String> newSymbols = new ArrayList<>();

        for (Stock stock : stocks) {
            if (stock == null || stock.getSymbol() == null) continue;
            stock.toHashData();

            String oldHash = existingHashes.get(stock.getSymbol());
            if (oldHash == null) {
                newSymbols.add(stock.getSymbol());
                changedStocks.add(stock);
            } else if (!oldHash.equals(stock.getHashData())) {
                changedStocks.add(stock);
            }
        }

        if (!changedStocks.isEmpty()) {
            stockRepositoryPort.saveAllStockSsi(changedStocks);
            stockCachePort.saveAll(changedStocks);
            stockKafkaPort.publishUpdatedStock(newSymbols);
            log.info("Synced {} changed stocks ({} new).", changedStocks.size(), newSymbols.size());
        }

        return new SyncStockResult(stocks.size(), changedStocks.size(), newSymbols);
    }

    private Map<String, String> getExistingHashes() {
        Map<String, String> cacheHashes = stockCachePort.getAllSymbolHashes();
        if (cacheHashes != null && !cacheHashes.isEmpty()) {
            return cacheHashes;
        }
        log.info("Redis cache is empty, falling back to database hashes...");
        Map<String, String> dbHashes = stockRepositoryPort.getAllSymbolHashes();
        return dbHashes != null ? dbHashes : Collections.emptyMap();
    }

}
