package com.vcc.bigdata.stock.application.dto;

import java.util.List;

public record SyncStockResult (
        int totalFetched,
        int totalChanged,
        List<String> newSymbols,
        List<String> updatedSymbols
) {}
