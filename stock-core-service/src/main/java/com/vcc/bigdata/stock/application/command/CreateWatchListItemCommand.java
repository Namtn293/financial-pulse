package com.vcc.bigdata.stock.application.command;

import java.util.UUID;

public record CreateWatchListItemCommand(
        String note,
        String symbol,
        Boolean isPinned,
        UUID watchListId
) {}
