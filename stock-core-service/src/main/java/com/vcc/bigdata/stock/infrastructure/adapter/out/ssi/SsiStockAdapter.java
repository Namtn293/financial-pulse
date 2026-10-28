package com.vcc.bigdata.stock.infrastructure.adapter.out.ssi;

import com.vcc.bigdata.stock.application.port.out.SsiStockPort;
import com.vcc.bigdata.stock.domain.model.identity.Stock;
import com.vcc.bigdata.stock.infrastructure.adapter.out.ssi.dto.SsiApiResponse;
import com.vcc.bigdata.stock.infrastructure.adapter.out.ssi.dto.StockDataSsiResponse;
import com.vcc.bigdata.stock.infrastructure.adapter.out.ssi.mapper.StockSsiMapper;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class SsiStockAdapter implements SsiStockPort {

    private final RestClient restClient;
    private final StockSsiMapper stockSsiMapper;

    @Value("${app.ssi.api.base-url:https://iboard-query.ssi.com.vn/stock/exchange}")
    private String ssiBaseUrl;

    @Value("${app.ssi.api.exchange:hose,hnx,upcom}")
    private List<String> exchanges;

    @Override
    @Retry(name = "stockApiRetry", fallbackMethod = "fetchSymbolInfoFallback")
    @RateLimiter(name = "stockApiLimiter")
    public List<Stock> fetchSymbolInfo() {
        log.info("Starting to fetch stock symbols from SSI for exchanges: {}", exchanges);
        List<Stock> allStocks = new ArrayList<>();

        for (String exchange : exchanges) {
            String url = ssiBaseUrl + "/" + exchange.trim().toLowerCase();
            try {
                SsiApiResponse<StockDataSsiResponse> response = restClient.get()
                        .uri(url)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.REFERER, "https://iboard.ssi.com.vn/")
                        .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {});

                if (response != null && response.data() != null) {
                    List<Stock> exchangeStocks = response.data().stream()
                            .map(stockSsiMapper::toDomain)
                            .toList();
                    allStocks.addAll(exchangeStocks);
                    log.info("Fetched {} symbols from exchange '{}'", exchangeStocks.size(), exchange);
                }
            } catch (Exception ex) {
                log.error("Error fetching symbols for exchange '{}': {}", exchange, ex.getMessage());
                throw ex;
            }
        }

        return allStocks;
    }

    public List<Stock> fetchSymbolInfoFallback(Throwable throwable) {
        log.error("Exhausted all retries fetching symbols from SSI. Error: {}", throwable.getMessage(), throwable);
        throw new RuntimeException("SSI API not response or interrupt: " + throwable.getMessage(), throwable);
    }
}