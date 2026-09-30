package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.mapper;

import com.vcc.bigdata.stock.domain.model.watchlist.WatchListItem;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity.WatchListItemsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WatchListItemsEntityPersistenceMapper {

    @Mapping(target = "watchList.id", source = "watchListId")
    WatchListItemsEntity toEntity(WatchListItem watchListItem);

    @Mapping(target = "watchListId", source = "watchList.id")
    WatchListItem toDomain(WatchListItemsEntity entity);
}
