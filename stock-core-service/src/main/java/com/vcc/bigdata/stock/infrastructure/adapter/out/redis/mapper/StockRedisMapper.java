package com.vcc.bigdata.stock.infrastructure.adapter.out.redis.mapper;

import com.vcc.bigdata.stock.domain.model.identity.Stock;
import com.vcc.bigdata.stock.infrastructure.adapter.out.redis.dto.StockRedisDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockRedisMapper {

    @Mapping(target = "companyName", source = "name")
    Stock toDomain(StockRedisDto stockRedisDto);

    @Mapping(target = "name", source = "companyName")
    StockRedisDto toDto(Stock stock);
}
