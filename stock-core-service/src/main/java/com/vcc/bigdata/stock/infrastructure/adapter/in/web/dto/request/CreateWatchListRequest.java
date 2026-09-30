package com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Request payload for create new watchlist")
public class CreateWatchListRequest {
    @NotBlank(message = "Name of watchlist must be not blank")
    String name;

    @NotEmpty(message = "List of stock symbol must be not empty")
    List<CreateWatchListItemsRequest> watchListItemsRequests;
}
