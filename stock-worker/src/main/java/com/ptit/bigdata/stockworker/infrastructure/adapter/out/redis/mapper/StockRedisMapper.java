package com.ptit.bigdata.stockworker.infrastructure.adapter.out.redis.mapper;

import com.ptit.bigdata.stockworker.domain.Stock;
import com.ptit.bigdata.stockworker.infrastructure.adapter.out.redis.dto.StockRedisDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockRedisMapper {

    Stock toDomain(StockRedisDto stockRedisDto);
}
