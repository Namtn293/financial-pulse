package com.ptit.bigdata.stockworker.application.service;

import com.ptit.bigdata.stockworker.application.port.in.StockUseCase;
import com.ptit.bigdata.stockworker.application.port.out.StockVietCadPort;
import com.ptit.bigdata.stockworker.domain.Stock;
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

    public StockService(StockVietCadPort stockVietCadPort) {
        this.stockVietCadPort = stockVietCadPort;
    }

    @Override
    public void updateStock(List<String> symbols) {
        if (symbols == null || symbols.isEmpty()) {
            log.warn("Symbols list is empty, skipping updateStock");
            return;
        }

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<Stock>> futures = symbols.stream()
                    .map(symbol -> CompletableFuture.supplyAsync(
                            () -> stockVietCadPort.getDataFromVietCad(symbol),
                            executor
                    ))
                    .toList();

            List<Stock> stocks = futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .toList();

            log.info("Fetched {}/{} stocks successfully from VietCad", stocks.size(), symbols.size());
            // check trong redis, lưu trong mysql -> xog
            // thiếu xử lý các ngoại lệ
        }
    }
}
