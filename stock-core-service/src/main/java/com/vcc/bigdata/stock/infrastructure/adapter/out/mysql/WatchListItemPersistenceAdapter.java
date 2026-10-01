package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql;

import com.vcc.bigdata.stock.application.port.out.WatchListItemRepositoryPort;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchListItem;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity.WatchListItemEntity;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.repository.WatchListItemJpaRepository;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.mapper.WatchListItemEntityPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WatchListItemPersistenceAdapter implements WatchListItemRepositoryPort {
    private final WatchListItemJpaRepository watchListItemJpaRepository;
    private final WatchListItemEntityPersistenceMapper watchListItemEntityPersistenceMapper;

    @Override
    public void saveAll(List<WatchListItem> watchListItems) {
        watchListItemJpaRepository.saveAll(
                watchListItems.stream()
                        .map(watchListItemEntityPersistenceMapper::toEntity)
                        .toList()
        );
    }

    @Override
    public WatchListItem save(WatchListItem watchListItem) {
        WatchListItemEntity watchListItemEntity = watchListItemJpaRepository.save(watchListItemEntityPersistenceMapper.toEntity(watchListItem));
        return watchListItemEntityPersistenceMapper.toDomain(watchListItemEntity);
    }
}
