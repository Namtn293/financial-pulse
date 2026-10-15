package com.vcc.bigdata.stock.infrastructure.adapter.out.mysql;

import com.vcc.bigdata.stock.application.port.out.StockRepositoryPort;
import com.vcc.bigdata.stock.domain.model.identity.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StockPersistenceAdapter implements StockRepositoryPort {

    private final JdbcTemplate jdbcTemplate;
    private static final int DEFAULT_CHUNK_SIZE = 500;

    /**
     * Batch upsert specific for SSI stock data (symbol, company_name, exchange, hash_data).
     * Preserves other metric fields if the record already exists in the database.
     */
    @Override
    @Transactional
    public int saveAllStockSsi(List<Stock> stocks) {
        if (stocks == null || stocks.isEmpty()) return 0;

        String sql = """
            INSERT INTO stock (
                symbol,
                company_name,
                exchange,
                hash_data,
                created_at,
                updated_at
            ) VALUES (?, ?, ?, ?, NOW(), NOW())
            ON DUPLICATE KEY UPDATE
                company_name = VALUES(company_name),
                exchange     = VALUES(exchange),
                hash_data    = VALUES(hash_data),
                updated_at   = NOW()
            """;

        int totalAffected = 0;
        for (int i = 0; i < stocks.size(); i += DEFAULT_CHUNK_SIZE) {
            List<Stock> batch = stocks.subList(i, Math.min(i + DEFAULT_CHUNK_SIZE, stocks.size()));

            int[] batchResult = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int idx) throws SQLException {
                    Stock stock = batch.get(idx);
                    ps.setString(1, stock.getSymbol() != null ? stock.getSymbol().trim().toUpperCase() : null);
                    ps.setString(2, stock.getCompanyName());
                    ps.setString(3, stock.getExchange());
                    ps.setString(4, stock.getHashData());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });

            totalAffected += Arrays.stream(batchResult).filter(r -> r >= 0).sum();
        }

        log.info("Batch upserted {} SSI stocks into database successfully", stocks.size());
        return totalAffected;
    }

    /**
     * Batch upsert for full Stock entity attributes.
     */
    @Override
    @Transactional
    public int saveAll(List<Stock> stocks) {
        if (stocks == null || stocks.isEmpty()) return 0;

        String sql = """
            INSERT INTO stock (
                symbol,
                company_name,
                exchange,
                industry,
                established_year,
                overview_summary,
                shares_outstanding,
                hash_data,
                vietcad_hash_data,
                created_at,
                updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
            ON DUPLICATE KEY UPDATE
                company_name       = VALUES(company_name),
                exchange           = VALUES(exchange),
                industry           = VALUES(industry),
                established_year   = VALUES(established_year),
                overview_summary   = VALUES(overview_summary),
                shares_outstanding = VALUES(shares_outstanding),
                hash_data          = VALUES(hash_data),
                vietcad_hash_data  = VALUES(vietcad_hash_data),
                updated_at         = NOW()
            """;

        int totalAffected = 0;
        for (int i = 0; i < stocks.size(); i += DEFAULT_CHUNK_SIZE) {
            List<Stock> batch = stocks.subList(i, Math.min(i + DEFAULT_CHUNK_SIZE, stocks.size()));

            int[] batchResult = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int idx) throws SQLException {
                    Stock stock = batch.get(idx);
                    ps.setString(1, stock.getSymbol() != null ? stock.getSymbol().trim().toUpperCase() : null);
                    ps.setString(2, stock.getCompanyName());
                    ps.setString(3, stock.getExchange());
                    ps.setString(4, stock.getIndustry());

                    if (stock.getEstablishedYear() != null) {
                        ps.setInt(5, stock.getEstablishedYear());
                    } else {
                        ps.setNull(5, Types.INTEGER);
                    }

                    ps.setString(6, stock.getOverviewSummary());

                    if (stock.getSharesOutstanding() != null) {
                        ps.setLong(7, stock.getSharesOutstanding());
                    } else {
                        ps.setNull(7, Types.BIGINT);
                    }

                    ps.setString(8, stock.getHashData());
                    ps.setString(9, stock.getVietcadHashData());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });

            totalAffected += Arrays.stream(batchResult).filter(r -> r >= 0).sum();
        }

        log.info("Batch upserted {} full stocks into database successfully", stocks.size());
        return totalAffected;
    }

    @Override
    public Map<String, String> getAllSymbolHashes() {
        String sql = "SELECT symbol, hash_data FROM stock WHERE hash_data IS NOT NULL";
        return jdbcTemplate.query(sql, rs -> {
            java.util.Map<String, String> map = new java.util.HashMap<>();
            while (rs.next()) {
                String symbol = rs.getString("symbol");
                String hashData = rs.getString("hash_data");
                if (symbol != null && hashData != null) {
                    map.put(symbol.toUpperCase(), hashData);
                }
            }
            return map;
        });
    }
}
