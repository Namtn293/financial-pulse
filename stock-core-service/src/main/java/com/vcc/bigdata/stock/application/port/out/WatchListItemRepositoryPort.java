package com.vcc.bigdata.stock.application.port.out;

import com.vcc.bigdata.stock.domain.model.watchlist.WatchListItem;

import java.util.List;

public interface WatchListItemRepositoryPort {
    void saveAll(List<WatchListItem> watchListItems);

    WatchListItem save(WatchListItem watchListItem);
}
