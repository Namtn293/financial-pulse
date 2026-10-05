package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.mapper;

import com.vcc.bigdata.stock.domain.model.identity.Stock;
import com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity.StockEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockEntityPersistenceMapper {
    StockEntity toEntity(Stock stock);
    Stock toDomain(StockEntity entity);

    List<StockEntity> toEntities(List<Stock> stocks);
    List<Stock> toDomains(List<StockEntity> entities);
}
