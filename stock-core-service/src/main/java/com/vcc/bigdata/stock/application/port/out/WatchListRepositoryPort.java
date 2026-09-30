package com.vcc.bigdata.stock.application.port.out;

import com.vcc.bigdata.stock.domain.model.watchlist.WatchList;

public interface WatchListRepositoryPort {
    boolean existByUserIdAndName(Long userId, String name);

    void save(WatchList watchList);
}
