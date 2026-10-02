package com.vcc.bigdata.stock.infrastructure.adapter.out.ssi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcc.bigdata.stock.application.port.out.SsiStockPort;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
@Slf4j
public class SsiStockAdapter implements SsiStockPort {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${app.ssi.api.symbols-url:https://iboard-query.ssi.com.vn/stock/exchange/hose}")
    private String ssiSymbolsUrl;

    @Override
    @Retry(name = "stockApiRetry")
    @RateLimiter(name = "stockApiLimiter", fallbackMethod = "fetchSymbolInfoFallback")
    public String fetchSymbolInfo() {
        log.info("Start calling to ssi");

        String rawResponse = restClient.get()
                .uri(ssiSymbolsUrl)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .retrieve()
                .body(String.class);
        try {
            Object json = objectMapper.readValue(rawResponse, Object.class);
            String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            System.out.println(prettyJson);
        } catch (Exception e) {
            log.warn("Can't fetch data from ssi {}", e.getMessage());
        }
        return null;
    }
}
