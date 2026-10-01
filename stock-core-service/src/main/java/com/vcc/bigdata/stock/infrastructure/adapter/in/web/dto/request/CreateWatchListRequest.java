package com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Request payload for create new watchlist")
public record CreateWatchListRequest(
        @NotBlank(message = "Name of watchlist must be not blank")
        String name,

        @Valid
        @NotEmpty(message = "List of stock symbol must be not empty")
        List<CreateWatchListItemRequest> watchListItemsRequests
) {}
