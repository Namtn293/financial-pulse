package com.ptit.bigdata.stockworker.infrastructure.adapter.out.redis;

import com.ptit.bigdata.stockworker.application.port.out.StockCachePort;
import com.ptit.bigdata.stockworker.domain.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class StockCacheAdapter implements StockCachePort {
    @Override
    public List<Stock> findAll() {

        return List.of();
    }

    @Override
    public void saveAll(Collection<Stock> stocks) {

    }
}
