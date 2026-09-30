package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql;

import com.vcc.bigdata.stock.application.port.out.WatchListRepositoryPort;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.repository.WatchListJpaRepository;

public class WatchListPersistenceAdapter implements WatchListRepositoryPort {
    private final WatchListJpaRepository watchListJpaRepository;

    public WatchListPersistenceAdapter(WatchListJpaRepository watchListJpaRepository) {
        this.watchListJpaRepository = watchListJpaRepository;
    }

    @Override
    public boolean existByUserIdAndName(Long userId, String name) {
        watchListJpaRepository.
        return false;
    }
}
