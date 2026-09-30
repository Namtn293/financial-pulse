package com.vcc.bigdata.stock.infrastructure.adapter.in.web.controller;

import com.vcc.bigdata.stock.application.command.CreateWatchListCommand;
import com.vcc.bigdata.stock.application.context.UserContext;
import com.vcc.bigdata.stock.application.port.in.WatchListUseCase;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.ApiResponse;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.request.CreateWatchListRequest;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.response.WatchListResponse;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.mapper.WatchListWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/watch-list")
@Tag(name = "Watchlist Management")
public class WatchListController {
    private final WatchListUseCase watchListUseCase;
    private final WatchListWebMapper watchListWebMapper;

    public WatchListController(WatchListUseCase watchListUseCase, WatchListWebMapper watchListWebMapper) {
        this.watchListUseCase = watchListUseCase;
        this.watchListWebMapper = watchListWebMapper;
    }

    @PostMapping
    @Operation(summary = "Create new stock watchlist")
    public ResponseEntity<ApiResponse<WatchListResponse>> create(
            @Valid @RequestBody CreateWatchListRequest request) {
        Long currentUserId = UserContext.getCurrentUserId()
                .orElseThrow(() -> new IllegalArgumentException("Missing or invalid user context."));

        CreateWatchListCommand command = watchListWebMapper.toCommand(request, currentUserId);


        return ResponseEntity.ok(ApiResponse.ok());
    }
}
