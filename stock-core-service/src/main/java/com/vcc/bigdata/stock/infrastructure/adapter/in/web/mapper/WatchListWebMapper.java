package com.vcc.bigdata.stock.infrastructure.adapter.in.web.mapper;

import com.vcc.bigdata.stock.application.command.CreateWatchListCommand;
import com.vcc.bigdata.stock.application.command.CreateWatchListItemsCommand;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.request.CreateWatchListItemsRequest;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.request.CreateWatchListRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class WatchListWebMapper {

    public CreateWatchListCommand toCommand(CreateWatchListRequest request, Long userId){
        List<CreateWatchListItemsCommand> list = request.getWatchListItemsRequests() == null
                ? Collections.emptyList()
                : request.getWatchListItemsRequests().stream()
                  .map(this::toItemCommand).toList();

        return new CreateWatchListCommand(userId, request.getName(), list);
    }

    public CreateWatchListItemsCommand toItemCommand(CreateWatchListItemsRequest request) {
        if (request == null) return null;
        return new CreateWatchListItemsCommand(
                request.getNote(),
                request.getSymbol(),
                request.getIsPinned()
        );
    }
}
