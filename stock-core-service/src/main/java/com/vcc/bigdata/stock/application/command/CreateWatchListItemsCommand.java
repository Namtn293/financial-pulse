package com.vcc.bigdata.stock.application.command;

public record CreateWatchListItemsCommand(
        String note,
        String symbol,
        Boolean isPinned
) {}
