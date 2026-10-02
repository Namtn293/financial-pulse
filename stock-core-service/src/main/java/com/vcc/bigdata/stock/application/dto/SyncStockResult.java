package com.vcc.bigdata.stock.application.dto;

import java.util.List;

public record SyncStockResult (
        int totalFetched,
        int totalInserted,
        int totalUpdated,
        int totalSkipped,
        long durationMs,
        List<String> newSymbols
) {}
