package com.vcc.bigdata.stock.infrastructure.adapter.out.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcc.bigdata.stock.application.port.out.StockCachePort;
import com.vcc.bigdata.stock.domain.model.identity.Stock;
import com.vcc.bigdata.stock.infrastructure.adapter.out.redis.dto.StockRedisDto;
import com.vcc.bigdata.stock.infrastructure.adapter.out.redis.mapper.StockRedisMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class StockCacheAdapter implements StockCachePort {
    private final StringRedisTemplate stringRedisTemplate;
    private final StockRedisMapper mapper;

    private static final String KEY_PREFIX = "stock:profile:";
    private static final String HASH_KEY = "stock:hashes";
    private static final String NULL_SENTINEL = "{__NULL__}";
    private static final int BATCH_SIZE = 500;
    private final ObjectMapper redisObjectMapper;

    @Override
    public void saveAll(Collection<Stock> stocks) {
        if (stocks == null || stocks.isEmpty()) return;

        try {
            List<Stock> validStocks = stocks.stream()
                    .filter(stock -> stock != null
                    && stock.getSymbol() != null
                    && !stock.getSymbol().isBlank())
                    .toList();

            for (int i = 0; i < validStocks.size(); i+= BATCH_SIZE) {
                List<Stock> batch = validStocks.subList(i, Math.min(i + BATCH_SIZE, validStocks.size()) - 1);
                executePipelineBatch(batch);
            }

            Map<String, String> hashBatch = new HashMap<>();
            for (Stock stock : validStocks) {
                if (stock.getSymbol() != null && stock.getHashData() != null) {
                    hashBatch.put(stock.getSymbol().trim().toUpperCase(), stock.getHashData());
                }
            }
            saveSymbolHashes(hashBatch);

        } catch (Exception e) {
            log.error("Error: not save stocks into redis with exception {}", e.getMessage());
        }
    }

    private void executePipelineBatch(List<Stock> batch) {
        Map<String, String> keyJsonMap = new HashMap<>();
        for (Stock stock : batch) {
            try {
                String key = buildKey(stock.getSymbol());
                stock.toHashData();

                StockRedisDto dto = mapper.toDto(stock);
                keyJsonMap.put(key, redisObjectMapper.writeValueAsString(dto));
            } catch (Exception e) {
                log.error("Skip this symbol {} with exception {}", stock.getSymbol(), e.getMessage());
            }
        }

        if (keyJsonMap.isEmpty()) {
            log.info("No symbol will be reserved");
            return;
        }

        stringRedisTemplate.opsForValue().multiSet(keyJsonMap);
    }

    private String buildKey(String symbol) {
        return KEY_PREFIX + symbol.trim().toUpperCase();
    }

    @Override
    public void save(Stock stock) {

    }

    @Override
    public Optional<Stock> findBySymbol(String symbol) {
        if (!StringUtils.hasText(symbol)) {
            return Optional.empty();
        }
        try {
            String rawJson = stringRedisTemplate.opsForValue().get(buildKey(symbol));

            if (!StringUtils.hasText(rawJson) || NULL_SENTINEL.equals(rawJson)) {
                return Optional.empty();
            }

            StockRedisDto dto = redisObjectMapper.readValue(rawJson, StockRedisDto.class);
            return Optional.ofNullable(mapper.toDomain(dto));
        } catch (Exception e) {
            log.error("Error read cache for symbol: {}", symbol, e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, Stock> findBySymbol(Collection<String> symbols) {
        if (symbols == null || symbols.isEmpty()) return Collections.emptyMap();

        try {
            List<String> keySymbols = symbols.stream()
                    .filter(StringUtils::hasText)
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .distinct()
                    .map(this::buildKey)
                    .toList();

            List<String> jsonResults = stringRedisTemplate.opsForValue().multiGet(keySymbols);
            if (jsonResults == null || jsonResults.isEmpty()) return Collections.emptyMap();

            Map<String, Stock> resultMaps = new HashMap<>((int) (jsonResults.size() / 0.75f) + 1);

            for (String rawJson : jsonResults) {
                if (!StringUtils.hasText(rawJson) || NULL_SENTINEL.equals(rawJson)) continue;

                try {
                    StockRedisDto dto = redisObjectMapper.readValue(rawJson, StockRedisDto.class);

                    Stock stock = mapper.toDomain(dto);
                    if (stock != null && stock.getSymbol() != null) {
                        resultMaps.put(stock.getSymbol().toUpperCase(), stock);
                    }
                } catch (Exception e) {
                    log.error("Error for read redis vale structure of symbol {}", e.getMessage());
                }
            }
            return resultMaps;
        } catch (Exception e) {
            log.error("Error loading multiGet from redis with exception {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    @Override
    public void cacheNullSymbol(String symbol) {

    }

    @Override
    public void deleteBySymbol(String symbol) {

    }

    @Override
    public Map<String, String> getAllSymbolHashes() {
        Map<String, String> results = new HashMap<>();

        ScanOptions options = ScanOptions.scanOptions()
                .match(KEY_PREFIX+"*")
                .count(500)
                .build();

        try (Cursor<String> cursor = stringRedisTemplate.scan(options)) {
            List<String> keys = new ArrayList<>();
            while (cursor.hasNext()) {
                keys.add(cursor.next());
                if (keys.size() >= 500) {
                    fetchAndExtractHashes(keys, results);
                    keys.clear();
                }
            }
            if (!keys.isEmpty()) fetchAndExtractHashes(keys, results);
        } catch (Exception e) {
            log.error("Error scanning keys from Redis: {}", e.getMessage(), e);
        }
        return results;
    }

    @Override
    public void saveSymbolHashes(Map<String, String> symbolHashes) {
        if (symbolHashes == null) return;

        try {
            stringRedisTemplate.opsForHash().putAll(HASH_KEY, symbolHashes);
            log.info("Saved {} symbol hashes into Redis key '{}'", symbolHashes.size(), HASH_KEY);
        } catch (Exception e) {
            log.error("Failed to save symbol hashes into Redis key {}: {}", HASH_KEY, e.getMessage());
        }
    }

    private void fetchAndExtractHashes(List<String> keys, Map<String, String> results) {
        List<String> jsonList = stringRedisTemplate.opsForValue().multiGet(keys);
        if (jsonList == null) return;

        for (String json : jsonList) {
            if (!StringUtils.hasText(json) || NULL_SENTINEL.equals(json)) continue;
            try {
                StockRedisDto dto = redisObjectMapper.readValue(json, StockRedisDto.class);
                if (dto != null && dto.getSymbol() != null && dto.getHashData() != null) {
                    results.put(dto.getSymbol().toUpperCase(), dto.getHashData());
                }
            } catch (Exception ignored) {}
        }
    }
}
