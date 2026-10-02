package com.vcc.bigdata.stock.application.service;

import com.vcc.bigdata.stock.application.dto.SyncStockResult;
import com.vcc.bigdata.stock.application.port.in.StockUseCase;
import com.vcc.bigdata.stock.application.port.out.SsiStockPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService implements StockUseCase {
    private final SsiStockPort ssiStockPort;

    @Override
    public SyncStockResult sync() {
        String s = ssiStockPort.fetchSymbolInfo();
        return null;
    }
}
