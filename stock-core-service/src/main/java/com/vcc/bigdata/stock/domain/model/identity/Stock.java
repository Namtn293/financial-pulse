package com.vcc.bigdata.stock.domain.model.identity;

import com.vcc.bigdata.stock.infrastructure.util.StockHashUtils;
import lombok.*;

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
    private Integer establishedYear;
    private String overviewSummary;
    private Long sharesOutstanding;
    private Instant updatedAt;
    private String hashData;
    private String vietcadHashData;

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", exchange='" + exchange + '\'' +
                '}';
    }

    public void toHashData() {
        this.hashData = StockHashUtils.hashStock(this.getSymbol(), this.getCompanyName(), this.getExchange());
    }
}
