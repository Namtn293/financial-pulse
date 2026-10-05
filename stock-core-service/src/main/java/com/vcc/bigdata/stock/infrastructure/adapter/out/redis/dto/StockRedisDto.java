package com.vcc.bigdata.stock.infrastructure.adapter.out.redis.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockRedisDto implements Serializable {
    private String symbol;
    private String name;
    private String exchange;
    private String hashData;
}
