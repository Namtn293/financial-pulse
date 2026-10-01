package com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.response;

import java.time.Instant;
import java.util.UUID;

public record WatchListItemResponse(
        UUID id,
        UUID watchListId,
        String symbol,
        String note,
        boolean pinned,
        Instant createdAt,
        Instant updatedAt
) {}
