package com.ptit.bigdata.stockworker.infrastructure.adapter.in.web;

import com.ptit.bigdata.stockworker.application.port.in.StockUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stock-test")
@Tag(name = "Test Stock Kafka")
@RequiredArgsConstructor
public class StockTestController {
    private final StockUseCase stockUseCase;

    @GetMapping("/stock-update")
    public void update() {
//        stockUseCase.updateStock();
    }
}
