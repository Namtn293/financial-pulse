package com.ptit.bigdata.stockworker.application.port.out;

import com.ptit.bigdata.stockworker.domain.Stock;

import java.util.Collection;
import java.util.List;

public interface StockCachePort {
    List<Stock> findAll();

    void saveAll(Collection<Stock> stocks);
}
