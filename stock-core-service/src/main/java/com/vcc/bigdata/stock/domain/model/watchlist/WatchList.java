package com.vcc.bigdata.stock.domain.model.watchlist;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WatchList {
    private UUID id;
    private UUID userId;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
}
