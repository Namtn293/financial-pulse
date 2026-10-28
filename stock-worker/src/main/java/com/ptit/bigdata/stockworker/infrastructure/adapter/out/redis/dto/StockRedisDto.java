package com.ptit.bigdata.stockworker.infrastructure.adapter.out.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockRedisDto implements Serializable {
    String industry;
    Long sharesOutstanding;
    String overviewSummary;
    Integer establishYear;
    String vietcadHashData;
}
