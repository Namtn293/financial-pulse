package com.vcc.bigdata.stock.infrastructure.adapter.in.web.controller;

import com.vcc.bigdata.stock.application.dto.SyncStockResult;
import com.vcc.bigdata.stock.application.port.in.StockUseCase;
import com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stock")
@Tag(name = "Stock Management")
@RequiredArgsConstructor
public class StockController {
    private final StockUseCase stockUseCase;

    @GetMapping("/sync")
    @Operation(summary = "Sync fetch data from 3rd")
    public ResponseEntity<ApiResponse<SyncStockResult>> sync(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(stockUseCase.sync(), "Sync success"));
    }
}
