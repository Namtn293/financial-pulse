package com.vcc.bigdata.stock.application.port.out;

import com.vcc.bigdata.stock.domain.model.identity.Stock;

import java.util.List;

public interface SsiStockPort {
    List<Stock> fetchSymbolInfo();
}
