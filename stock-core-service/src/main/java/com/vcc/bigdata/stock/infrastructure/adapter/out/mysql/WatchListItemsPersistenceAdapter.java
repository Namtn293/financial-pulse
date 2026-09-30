package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql;

import com.vcc.bigdata.stock.application.port.out.WatchListItemsRepositoryPort;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchListItem;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.repository.WatchListItemsJpaRepository;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.mapper.WatchListItemsEntityPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WatchListItemsPersistenceAdapter implements WatchListItemsRepositoryPort {
    private final WatchListItemsJpaRepository watchListItemsJpaRepository;
    private final WatchListItemsEntityPersistenceMapper watchListItemsEntityPersistenceMapper;

    @Override
    public void saveAll(List<WatchListItem> watchListItems) {
        watchListItemsJpaRepository.saveAll(
                watchListItems.stream()
                        .map(watchListItemsEntityPersistenceMapper::toEntity)
                        .toList()
        );
    }
}
