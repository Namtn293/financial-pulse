package com.vcc.bigdata.stock.application.port.in;

import com.vcc.bigdata.stock.application.command.CreateWatchListCommand;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchList;

/** Inbound port for managing watchlist. */
public interface WatchListUseCase {
    WatchList createWatchList(CreateWatchListCommand createWatchListCommand);
}
