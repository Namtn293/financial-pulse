package com.vcc.bigdata.stock.infrastructure.adapter.out.ssi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StockDataSsiResponse (
        @JsonProperty("stockSymbol") String stockSymbol,
        @JsonProperty("companyNameVi") String companyNameVi,
        @JsonProperty("exchange") String exchange
) {}
