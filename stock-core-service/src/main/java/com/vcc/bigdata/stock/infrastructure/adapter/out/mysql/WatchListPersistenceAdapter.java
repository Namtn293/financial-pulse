package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql;

import com.vcc.bigdata.stock.application.port.out.WatchListRepositoryPort;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchList;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchListItem;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity.WatchListEntity;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.repository.WatchListJpaRepository;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.mapper.WatchListEntityPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

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
    public WatchList save(WatchList watchList) {
        WatchListEntity watchListEntity = watchListJpaRepository.save(watchListEntityPersistenceMapper.toEntity(watchList));
        return watchListEntityPersistenceMapper.toDomain(watchListEntity);
    }

    @Override
    public List<WatchList> getWatchLists(Long userId, Pageable pageable) {
        return watchListJpaRepository.getWatchListEntitiesByUserId(userId, pageable).getContent()
                .stream()
                .map(watchListEntityPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existByWatchListId(UUID id) {
        return watchListJpaRepository.existsById(id);
    }
}
