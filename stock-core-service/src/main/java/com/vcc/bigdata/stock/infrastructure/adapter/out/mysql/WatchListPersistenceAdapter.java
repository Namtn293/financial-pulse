package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql;

import com.vcc.bigdata.stock.application.port.out.WatchListRepositoryPort;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchList;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity.WatchListEntity;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.repository.WatchListJpaRepository;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.mapper.WatchListEntityPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WatchListPersistenceAdapter implements WatchListRepositoryPort {
    private final WatchListJpaRepository watchListJpaRepository;
    private final WatchListEntityPersistenceMapper watchListEntityPersistenceMapper;

    @Override
    public boolean existByUserIdAndName(Long userId, String name) {
        return watchListJpaRepository.existsByUserIdAndName(userId, name);
    }

    @Override
    public void save(WatchList watchList) {
        watchListJpaRepository.save(watchListEntityPersistenceMapper.toEntity(watchList));
    }


}
