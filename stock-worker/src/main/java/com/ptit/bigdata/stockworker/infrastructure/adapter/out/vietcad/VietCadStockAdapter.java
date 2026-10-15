package com.ptit.bigdata.stockworker.infrastructure.adapter.out.vietcad;

import com.ptit.bigdata.stockworker.application.port.out.StockVietCadPort;
import com.ptit.bigdata.stockworker.domain.Stock;
import com.ptit.bigdata.stockworker.infrastructure.adapter.out.vietcad.dto.StockDataVietCadResponse;
import com.ptit.bigdata.stockworker.infrastructure.adapter.out.vietcad.dto.VietCadApiResponse;
import com.ptit.bigdata.stockworker.infrastructure.adapter.out.vietcad.mapper.StockVietCapMapper;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
@Slf4j
public class VietCadStockAdapter implements StockVietCadPort {
    private final RestClient restClient;
    private final StockVietCapMapper stockVietCapMapper;

    @Value("${app.vietcad.api.details}")
    private String detailsUrl;

    public VietCadStockAdapter(RestClient restClient, StockVietCapMapper stockVietCapMapper) {
        this.restClient = restClient;
        this.stockVietCapMapper = stockVietCapMapper;
    }

    @Override
    @RateLimiter(name = "vietcad", fallbackMethod = "getDataFromVietCadFallback")
    public Stock getDataFromVietCad(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            return null;
        }

        VietCadApiResponse<StockDataVietCadResponse> symbolInfos = restClient.get()
                .uri(detailsUrl + "?ticker=" + symbol.trim().toUpperCase())
                .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (symbolInfos != null && symbolInfos.getData() != null) {
            log.info("Get data success with info symbol={}: {}", symbol, symbolInfos.getData());
            Stock stock = stockVietCapMapper.toDomain(symbolInfos.getData());
            stock.setSymbol(symbol.trim().toUpperCase());
            String listingDate = symbolInfos.getData().getListingDate();
            if (listingDate != null && listingDate.length() >= 4) {
                try {
                    stock.setEstablishedYear(Integer.parseInt(listingDate.substring(0, 4)));
                } catch (NumberFormatException e) {
                    log.warn("Cannot parse established year from listingDate '{}' for symbol {}", listingDate, symbol);
                }
            }
            stock.toVietcadHashData();
            return stock;
        }
        return null;
    }

    public Stock getDataFromVietCadFallback(String symbol, RequestNotPermitted ex) {
        log.warn("Rate limit exceeded for VietCad API on symbol {}: {}", symbol, ex.getMessage());
        return null;
    }

    public Stock getDataFromVietCadFallback(String symbol, Throwable ex) {
        log.error("Error fetching VietCad data for symbol {}: {}", symbol, ex.getMessage());
        return null;
    }
}
