package com.vcc.bigdata.stock.application.port.in;


import com.vcc.bigdata.stock.application.command.CreateWatchListItemCommand;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchListItem;

public interface WatchListItemUseCase {
    WatchListItem create(CreateWatchListItemCommand createWatchListItemCommand);
}
