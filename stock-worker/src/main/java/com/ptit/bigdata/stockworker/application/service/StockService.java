package com.ptit.bigdata.stockworker.application.service;

import com.ptit.bigdata.stockworker.application.port.in.StockUseCase;
import com.ptit.bigdata.stockworker.application.port.out.StockCachePort;
import com.ptit.bigdata.stockworker.application.port.out.StockRepositoryPort;
import com.ptit.bigdata.stockworker.application.port.out.StockVietCadPort;
import com.ptit.bigdata.stockworker.domain.Stock;
import com.ptit.bigdata.stockworker.infrastructure.adapter.out.vietcad.VietCadStockAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class StockService implements StockUseCase {
    private final StockVietCadPort stockVietCadPort;
    private final StockCachePort stockCachePort;
    private final StockRepositoryPort stockRepositoryPort;

    public StockService(StockRepositoryPort stockRepositoryPort, StockCachePort stockCachePort, StockVietCadPort stockVietCadPort) {
        this.stockVietCadPort = stockVietCadPort;
        this.stockCachePort = stockCachePort;
        this.stockRepositoryPort = stockRepositoryPort;
    }

    @Override
    public void updateStock(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            log.warn("Symbol is empty, skipping updateStock");
            return;
        }

        String cleanSymbol = symbol.trim().toUpperCase();
        log.info("Fetching detailed profile for symbol {} from VietCad", cleanSymbol);

        Stock stock = stockVietCadPort.getDataFromVietCad(cleanSymbol);

        if (stock != null) {
            stockRepositoryPort.saveAll(List.of(stock));
            log.info("Successfully updated symbol {} into MySQL", cleanSymbol);
        }
    }
}
