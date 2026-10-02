package com.vcc.bigdata.stock.domain.model.identity;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Stock {
    private String symbol;
    private String companyName;
    private String exchange;
    private String industry;
    private String ceoName;
    private Integer establishedYear;
    private String website;
    private String overviewSummary;
    private Long sharesOutstanding;
    private BigDecimal pe;
    private BigDecimal pb;
    private BigDecimal roe;
    private BigDecimal rsi14;
    private BigDecimal ma20;
    private BigDecimal ma50;
    private Long avgVolume20d;
    private Instant updatedAt;

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", exchange='" + exchange + '\'' +
                '}';
    }
}
