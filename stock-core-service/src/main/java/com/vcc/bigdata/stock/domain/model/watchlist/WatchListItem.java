package com.vcc.bigdata.stock.domain.model.watchlist;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WatchListItem {
    private UUID id;
    private UUID watchListId;
    private String symbol;
    private String note;
    private boolean pinned;
    private Instant createdAt;
    private Instant updatedAt;
}
