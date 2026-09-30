package com.vcc.bigdata.stock.domain.service;

import com.vcc.bigdata.stock.application.command.CreateWatchListCommand;
import com.vcc.bigdata.stock.application.port.in.WatchListUseCase;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchList;
import org.springframework.transaction.annotation.Transactional;

public class WatchListService implements WatchListUseCase {


    @Transactional
    @Override
    public WatchList createWatchList(CreateWatchListCommand createWatchListCommand) {

        return null;
    }
}
