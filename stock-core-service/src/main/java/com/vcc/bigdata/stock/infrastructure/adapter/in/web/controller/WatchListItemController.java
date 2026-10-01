package com.vcc.bigdata.stock.infrastructure.adapter.in.web.controller;

import com.vcc.bigdata.stock.application.command.CreateWatchListItemCommand;
import com.vcc.bigdata.stock.application.port.in.WatchListItemUseCase;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchListItem;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.ApiResponse;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.request.CreateWatchListItemRequest;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.response.WatchListItemResponse;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.mapper.WatchListWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/watch-list-items")
@RequiredArgsConstructor
@Tag(name = "WatchList Item Management")
public class WatchListItemController {
    private final WatchListItemUseCase watchListItemUseCase;
    private final WatchListWebMapper watchListWebMapper;

    @PostMapping("/create")
    @Operation(summary = "Insert new watchlist item into a watchlist")
    public ResponseEntity<ApiResponse<WatchListItemResponse>> create(
            @RequestBody CreateWatchListItemRequest createWatchListItemRequest
            ) {
        CreateWatchListItemCommand command = watchListWebMapper.toItemCommand(createWatchListItemRequest);

        WatchListItem watchListItem = watchListItemUseCase.create(command);
        WatchListItemResponse watchListItemResponse = watchListWebMapper.toItemResponse(watchListItem);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(watchListItemResponse, "Create new watchList item successfully"));
    }
}
