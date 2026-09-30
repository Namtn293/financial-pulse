package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "watch_list_item")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class WatchListItemsEntity {
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watchlist_id", nullable = false)
    private WatchListEntity watchList;

    @Column(name = "symbol", nullable = false, length = 20)
    private String symbol;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "is_pinned", nullable = false)
    private boolean pinned;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
