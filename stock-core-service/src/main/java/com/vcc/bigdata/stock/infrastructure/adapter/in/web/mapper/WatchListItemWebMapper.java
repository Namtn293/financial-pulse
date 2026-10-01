package com.vcc.bigdata.stock.infrastructure.adapter.in.web.mapper;

import com.vcc.bigdata.stock.application.command.CreateWatchListItemCommand;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchListItem;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.request.CreateWatchListItemRequest;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.response.WatchListResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WatchListItemWebMapper {
    WatchListResponse toResponse(WatchListItem watchListItem);

    CreateWatchListItemCommand toItemCommand(CreateWatchListItemRequest request);

}
