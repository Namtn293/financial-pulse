package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(
    name = "stock",
    indexes = {
        @Index(name = "idx_stock_exchange", columnList = "exchange"),
        @Index(name = "idx_stock_industry", columnList = "industry")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockEntity {

    @Id
    @Column(name = "symbol", length = 20, nullable = false, updatable = false)
    private String symbol;

    @Column(name = "company_name", length = 255, nullable = false)
    private String companyName;

    @Column(name = "exchange", length = 10, nullable = false)
    private String exchange;

    @Column(name = "industry", length = 100)
    private String industry;

    @Column(name = "ceo_name", length = 100)
    private String ceoName;

    @Column(name = "established_year")
    private Integer establishedYear;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "overview_summary", columnDefinition = "TEXT")
    private String overviewSummary;

    @Column(name = "shares_outstanding")
    private Long sharesOutstanding;

    @Column(name = "pe", precision = 10, scale = 2)
    private BigDecimal pe;

    @Column(name = "pb", precision = 10, scale = 2)
    private BigDecimal pb;

    @Column(name = "roe", precision = 10, scale = 2)
    private BigDecimal roe;

    @Column(name = "rsi_14", precision = 10, scale = 2)
    private BigDecimal rsi14;

    @Column(name = "ma_20", precision = 15, scale = 2)
    private BigDecimal ma20;

    @Column(name = "ma_50", precision = 15, scale = 2)
    private BigDecimal ma50;

    @Column(name = "avg_volume_20d")
    private Long avgVolume20d;

    @Column(name = "hash_data", length = 64)
    private String hashData;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockEntity that)) return false;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
