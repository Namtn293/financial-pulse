package com.vcc.bigdata.stock.infrastructure.adapter.out.ssi.mapper;

import com.vcc.bigdata.stock.domain.model.identity.Stock;
import com.vcc.bigdata.stock.infrastructure.adapter.out.ssi.dto.StockDataSsiResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockSsiMapper {

    @Mapping(target = "symbol", source = "stockSymbol")
    @Mapping(target = "companyName", source = "companyNameVi")
    @Mapping(target = "exchange", source = "exchange")
    Stock toDomain(StockDataSsiResponse stockDataSsiResponse);

    List<Stock> toDomains(List<StockDataSsiResponse> stockDataSsiResponses);
}
