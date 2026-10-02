package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql;

import com.vcc.bigdata.stock.application.port.out.StockRepositoryPort;
import com.vcc.bigdata.stock.domain.model.identity.Stock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StockPersistenceAdapter implements StockRepositoryPort {
    @Override
    public int saveAll(List<Stock> stocks) {
        return 0;
    }
}
