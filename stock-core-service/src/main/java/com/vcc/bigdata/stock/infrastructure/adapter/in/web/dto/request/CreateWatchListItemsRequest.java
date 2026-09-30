package com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Each item symbolize for a stock code")
public class CreateWatchListItemsRequest {

    private String note;

    @NotBlank(message = "Symbol must be not blank")
    private String symbol;

    private Boolean isPinned = false;
}
