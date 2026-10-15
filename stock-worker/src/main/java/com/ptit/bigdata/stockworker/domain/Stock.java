package com.ptit.bigdata.stockworker.domain;

import com.ptit.bigdata.stockworker.infrastructure.util.StockHashUtils;
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
                ", industry='" + industry + '\'' +
                ", vietcadHashData='" + vietcadHashData + '\'' +
                '}';
    }

    public void toVietcadHashData() {
        this.vietcadHashData = StockHashUtils.hashVietcadStock(this.industry, this.overviewSummary, this.sharesOutstanding, this.establishedYear);
    }
}
