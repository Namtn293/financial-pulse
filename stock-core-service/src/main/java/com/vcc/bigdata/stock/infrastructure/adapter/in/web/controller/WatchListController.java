package com.vcc.bigdata.stock.infrastructure.adapter.in.web.controller;

import com.vcc.bigdata.stock.application.command.CreateWatchListCommand;
import com.vcc.bigdata.stock.application.context.UserContext;
import com.vcc.bigdata.stock.application.port.in.WatchListUseCase;
import com.vcc.bigdata.stock.domain.model.watchlist.WatchList;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.ApiResponse;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.request.CreateWatchListRequest;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.response.WatchListResponse;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.mapper.WatchListWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/watch-list")
@RequiredArgsConstructor
@Tag(name = "Watchlist Management")
public class WatchListController {

    private final WatchListUseCase watchListUseCase;
    private final WatchListWebMapper watchListWebMapper;

    @PostMapping
    @Operation(summary = "Create new stock watchlist")
    public ResponseEntity<ApiResponse<WatchListResponse>> create(
            @Parameter(name = "X-User-Id", in = ParameterIn.HEADER, description = "User ID", required = true, example = "1001")
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @Valid @RequestBody CreateWatchListRequest request) {
        Long currentUserId = UserContext.getCurrentUserId()
                .orElseThrow(() -> new IllegalArgumentException("Missing or invalid user context."));

        CreateWatchListCommand command = watchListWebMapper.toCommand(request, currentUserId);
        WatchList created = watchListUseCase.createWatchList(command);
        WatchListResponse response = watchListWebMapper.toResponse(created);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "Watchlist created successfully"));
    }
}

