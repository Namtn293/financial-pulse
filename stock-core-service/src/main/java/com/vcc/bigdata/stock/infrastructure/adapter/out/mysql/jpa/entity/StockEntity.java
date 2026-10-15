package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(name = "established_year")
    private Integer establishedYear;

    @Column(name = "overview_summary", columnDefinition = "TEXT")
    private String overviewSummary;

    @Column(name = "shares_outstanding")
    private Long sharesOutstanding;

    @Column(name = "hash_data", length = 64)
    private String hashData;

    @Column(name = "vietcad_hash_data", length = 64)
    private String vietcadHashData;

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
