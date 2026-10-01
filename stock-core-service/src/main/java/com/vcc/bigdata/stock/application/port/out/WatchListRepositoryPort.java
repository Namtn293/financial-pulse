package com.vcc.bigdata.stock.application.port.out;

import com.vcc.bigdata.stock.domain.model.watchlist.WatchList;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchListItem;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface WatchListRepositoryPort {
    boolean existByUserIdAndName(Long userId, String name);

    WatchList save(WatchList watchList);

    List<WatchList> getWatchLists(Long userId, Pageable pageable);

    boolean existByWatchListId(UUID id);
}
