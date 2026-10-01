package com.vcc.bigdata.stock.infrastructure.adapter.in.web.mapper;

import com.vcc.bigdata.stock.application.command.CreateWatchListCommand;
import com.vcc.bigdata.stock.application.command.CreateWatchListItemCommand;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchList;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchListItem;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.request.CreateWatchListItemRequest;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.request.CreateWatchListRequest;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.response.WatchListItemResponse;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.response.WatchListResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface WatchListWebMapper {

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "items", source = "request.watchListItemsRequests")
    CreateWatchListCommand toCommand(CreateWatchListRequest request, Long userId);

    CreateWatchListItemCommand toItemCommand(CreateWatchListItemRequest request);

    WatchListResponse toResponse(WatchList watchList);

    List<WatchListResponse> toResponses(List<WatchList> watchLists);

    WatchListItemResponse toItemResponse(WatchListItem watchListItem);

    List<WatchListItemResponse> toItemResponses(List<WatchListItem> watchListItems);
}
