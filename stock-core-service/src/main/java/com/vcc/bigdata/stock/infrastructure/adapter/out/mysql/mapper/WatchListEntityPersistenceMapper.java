package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.mapper;


import com.vcc.bigdata.stock.domain.model.watchlist.WatchList;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity.WatchListEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WatchListEntityPersistenceMapper {
    WatchListEntity toEntity(WatchList watchList);
    WatchList toDomain(WatchListEntity entity);
}
