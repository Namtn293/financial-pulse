package com.vcc.bigdata.stock.infrastructure.adapter.in.web.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class WatchListResponse {
    private UUID id;
    private Long userId;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
}
