package com.ptit.bigdata.stockworker.infrastructure.adapter.out.vietcad.mapper;

import com.ptit.bigdata.stockworker.domain.Stock;
import com.ptit.bigdata.stockworker.infrastructure.adapter.out.vietcad.dto.StockDataVietCadResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockVietCapMapper {

    @Mapping(target = "industry", source = "sectorVn")
    @Mapping(target = "sharesOutstanding", source = "issueShare")
    @Mapping(target = "overviewSummary", source = "profile")
    @Mapping(target = "symbol", ignore = true)
    @Mapping(target = "companyName", ignore = true)
    @Mapping(target = "exchange", ignore = true)
    @Mapping(target = "establishedYear", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "hashData", ignore = true)
    @Mapping(target = "vietcadHashData", ignore = true)
    Stock toDomain(StockDataVietCadResponse response);
}
