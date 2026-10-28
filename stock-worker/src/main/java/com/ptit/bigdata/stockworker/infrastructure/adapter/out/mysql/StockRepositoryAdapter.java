package com.ptit.bigdata.stockworker.infrastructure.adapter.out.mysql;

import com.ptit.bigdata.stockworker.application.port.out.StockRepositoryPort;
import com.ptit.bigdata.stockworker.domain.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class StockRepositoryAdapter implements StockRepositoryPort {
    private final JdbcTemplate jdbcTemplate;
    private static final int BATCH_SIZE = 100;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(Collection<Stock> stocks) {
        if (stocks == null || stocks.isEmpty()) return;

        List<Stock> validStocks = stocks.stream()
                .filter(stock -> stock != null && stock.getSymbol() != null && !stock.getSymbol().isBlank())
                .toList();

        if (validStocks.isEmpty()) return;

        String sql = """
                UPDATE stock
                SET industry = ?,
                    established_year = ?,
                    overview_outstanding = ?,
                    vietcad_hash_data = ?,
                    updated_at = NOW()
                WHERE symbol = ?
                """;

        int totalUpdated = 0;
        for (int i = 0; i < validStocks.size(); i+=BATCH_SIZE) {
            List<Stock> batch = validStocks.subList(i,Math.min(i + BATCH_SIZE, validStocks.size() - 1));

            int[] result = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int idx) throws SQLException {
                    Stock stock = batch.get(idx);

                    ps.setString(1, stock.getIndustry());

                    if (stock.getEstablishedYear() != null) {
                        ps.setInt(2, stock.getEstablishedYear());
                    } else {
                        ps.setNull(2, Types.INTEGER);
                    }
                    ps.setString(3, stock.getOverviewSummary());

                    if (stock.getSharesOutstanding() != null) {
                        ps.setLong(4, stock.getSharesOutstanding());
                    } else {
                        ps.setNull(4, Types.BIGINT);
                    }

                    ps.setString(5, stock.getVietcadHashData());

                    ps.setString(6, stock.getSymbol().trim().toUpperCase());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });

            for (int r : result) {
                if (r > 0) totalUpdated += r;
            }
        }

        log.info("Batch updated {}/{} stocks in database successfully", totalUpdated, validStocks.size());
    }
}
