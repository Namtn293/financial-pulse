package com.ptit.bigdata.stockworker.infrastructure.adapter.out.vietcad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDataVietCadResponse {
    @JsonProperty("sectorVn")
    private String sectorVn;

    @JsonProperty("issueShare")
    private Long issueShare;

    @JsonProperty("profile")
    private String profile;

    @JsonProperty("listingDate")
    private String listingDate;

    @Override
    public String toString() {
        return "StockDataVietCadResponse{" +
                "sectorVn='" + sectorVn + '\'' +
                ", issueShare=" + issueShare +
                ", profile='" + profile + '\'' +
                ", listingDate='" + listingDate + '\'' +
                '}';
    }
}
