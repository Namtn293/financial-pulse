package com.vcc.bigdata.stock.infrastructure.adapter.out.ssi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SsiApiResponse<T> (
        @JsonProperty("code") String code,
        @JsonProperty("message") String message,
        @JsonProperty("data") List<T> data
) {}
