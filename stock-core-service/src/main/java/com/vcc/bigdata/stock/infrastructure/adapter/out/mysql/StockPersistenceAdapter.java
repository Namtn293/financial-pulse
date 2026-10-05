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
                ceo_name,
                established_year,
                website,
                overview_summary,
                shares_outstanding,
                pe,
                pb,
                roe,
                rsi_14,
                ma_20,
                ma_50,
                avg_volume_20d,
                hash_data,
                created_at,
                updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
            ON DUPLICATE KEY UPDATE
                company_name       = VALUES(company_name),
                exchange           = VALUES(exchange),
                industry           = VALUES(industry),
                ceo_name           = VALUES(ceo_name),
                established_year   = VALUES(established_year),
                website            = VALUES(website),
                overview_summary   = VALUES(overview_summary),
                shares_outstanding = VALUES(shares_outstanding),
                pe                 = VALUES(pe),
                pb                 = VALUES(pb),
                roe                = VALUES(roe),
                rsi_14             = VALUES(rsi_14),
                ma_20              = VALUES(ma_20),
                ma_50              = VALUES(ma_50),
                avg_volume_20d     = VALUES(avg_volume_20d),
                hash_data          = VALUES(hash_data),
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
                    ps.setString(5, stock.getCeoName());

                    if (stock.getEstablishedYear() != null) {
                        ps.setInt(6, stock.getEstablishedYear());
                    } else {
                        ps.setNull(6, Types.INTEGER);
                    }

                    ps.setString(7, stock.getWebsite());
                    ps.setString(8, stock.getOverviewSummary());

                    if (stock.getSharesOutstanding() != null) {
                        ps.setLong(9, stock.getSharesOutstanding());
                    } else {
                        ps.setNull(9, Types.BIGINT);
                    }

                    ps.setBigDecimal(10, stock.getPe());
                    ps.setBigDecimal(11, stock.getPb());
                    ps.setBigDecimal(12, stock.getRoe());
                    ps.setBigDecimal(13, stock.getRsi14());
                    ps.setBigDecimal(14, stock.getMa20());
                    ps.setBigDecimal(15, stock.getMa50());

                    if (stock.getAvgVolume20d() != null) {
                        ps.setLong(16, stock.getAvgVolume20d());
                    } else {
                        ps.setNull(16, Types.BIGINT);
                    }

                    ps.setString(17, stock.getHashData());
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
