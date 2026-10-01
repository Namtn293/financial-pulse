package com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.response;

import java.time.Instant;
import java.util.UUID;

public record WatchListResponse(
        UUID id,
        Long userId,
        String name,
        Instant createdAt,
        Instant updatedAt
) {}
