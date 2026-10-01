package com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Schema(description = "Each item symbolize for a stock code")
public record CreateWatchListItemRequest(
        String note,

        @NotBlank(message = "Symbol must be not blank")
        String symbol,

        Boolean isPinned,

        UUID watchListId
) {
    public CreateWatchListItemRequest {
        if (isPinned == null) {
            isPinned = false;
        }
    }
}
