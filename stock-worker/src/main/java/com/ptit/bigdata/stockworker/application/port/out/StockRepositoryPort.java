package com.ptit.bigdata.stockworker.application.port.out;

import com.ptit.bigdata.stockworker.domain.Stock;

import java.util.Collection;

public interface StockRepositoryPort {
    void saveAll(Collection<Stock> stocks);
}
