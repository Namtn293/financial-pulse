package com.vcc.bigdata.stock.application.service;

import com.vcc.bigdata.stock.application.command.CreateWatchListItemCommand;
import com.vcc.bigdata.stock.application.port.in.WatchListItemUseCase;
import com.vcc.bigdata.stock.application.port.out.WatchListItemRepositoryPort;
import com.vcc.bigdata.stock.application.port.out.WatchListRepositoryPort;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchListItem;
import com.vcc.bigdata.stock.util.UuidUtils;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WatchListItemService implements WatchListItemUseCase {

    private final WatchListRepositoryPort watchListRepositoryPort;
    private final WatchListItemRepositoryPort watchListItemRepositoryPort;

    @Override
    public WatchListItem create(CreateWatchListItemCommand createWatchListItemCommand) {
        if (!watchListRepositoryPort.existByWatchListId(createWatchListItemCommand.watchListId())){
            throw new ResourceNotFoundException("WatchListId not exist.");
        }

        WatchListItem watchListItem = WatchListItem.builder()
                .note(createWatchListItemCommand.note())
                .symbol(createWatchListItemCommand.symbol())
                .pinned(createWatchListItemCommand.isPinned())
                .id(UuidUtils.generateUuidV7())
                .watchListId(createWatchListItemCommand.watchListId())
                .build();

        return watchListItemRepositoryPort.save(watchListItem);
    }
}
