package com.ptit.bigdata.stockworker.infrastructure.adapter.out.mysql;

import com.ptit.bigdata.stockworker.application.port.out.StockRepositoryPort;
import com.ptit.bigdata.stockworker.domain.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
@Component
@Slf4j
public class StockRepositoryAdapter implements StockRepositoryPort {
    @Override
    public void saveAll(Collection<Stock> stocks) {

    }
}
