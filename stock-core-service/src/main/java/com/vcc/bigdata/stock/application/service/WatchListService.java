package com.vcc.bigdata.stock.application.service;

import com.vcc.bigdata.stock.application.command.CreateWatchListCommand;
import com.vcc.bigdata.stock.application.port.in.WatchListUseCase;
import com.vcc.bigdata.stock.application.port.out.WatchListItemRepositoryPort;
import com.vcc.bigdata.stock.application.port.out.WatchListRepositoryPort;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchList;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchListItem;
import com.vcc.bigdata.stock.util.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WatchListService implements WatchListUseCase {

    private final WatchListRepositoryPort watchListRepositoryPort;
    private final WatchListItemRepositoryPort watchListItemRepositoryPort;

    @Override
    @Transactional
    public WatchList createWatchList(CreateWatchListCommand createWatchListCommand) {
        log.info("Creating watchlist '{}' for userId: {}", createWatchListCommand.name(), createWatchListCommand.userId());

        if (watchListRepositoryPort.existByUserIdAndName(createWatchListCommand.userId(), createWatchListCommand.name())) {
            throw new IllegalArgumentException("Watchlist with name '" + createWatchListCommand.name() + "' already exists.");
        }

        UUID watchlistId = UuidUtils.generateUuidV7();
        Instant now = Instant.now();

        List<WatchListItem> watchListItems = createWatchListCommand.items() == null
                ? Collections.emptyList()
                : createWatchListCommand.items()
                  .stream()
                  .map(itemCmd -> WatchListItem.builder()
                          .id(UuidUtils.generateUuidV7())
                          .watchListId(watchlistId)
                          .symbol(itemCmd.symbol())
                          .note(itemCmd.note())
                          .pinned(Boolean.TRUE.equals(itemCmd.isPinned()))
                          .createdAt(now)
                          .updatedAt(now)
                          .build())
                  .toList();

        WatchList watchList = WatchList.builder()
                .id(watchlistId)
                .userId(createWatchListCommand.userId())
                .name(createWatchListCommand.name())
                .build();

        WatchList list = watchListRepositoryPort.save(watchList);
        watchListItemRepositoryPort.saveAll(watchListItems);

        return list;
    }

    @Override
    public List<WatchList> getWatchLists(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return watchListRepositoryPort.getWatchLists(userId, pageable);
    }
}

