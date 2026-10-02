package com.vcc.bigdata.stock.application.port.in;

import com.vcc.bigdata.stock.application.dto.SyncStockResult;

public interface StockUseCase {
    SyncStockResult sync();
}
