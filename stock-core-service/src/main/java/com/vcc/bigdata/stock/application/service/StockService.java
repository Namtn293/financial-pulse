package com.vcc.bigdata.stock.application.service;

import com.vcc.bigdata.stock.application.dto.SyncStockResult;
import com.vcc.bigdata.stock.application.port.in.StockUseCase;
import com.vcc.bigdata.stock.application.port.out.SsiStockPort;
import com.vcc.bigdata.stock.domain.model.identity.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService implements StockUseCase {
    private final SsiStockPort ssiStockPort;

    @Override
    public SyncStockResult sync() {
        List<Stock> stocks = ssiStockPort.fetchSymbolInfo();
        stocks.forEach(stock -> {
            System.out.println(stock.toString());
            System.out.println();
        });
        return null;
    }
}
