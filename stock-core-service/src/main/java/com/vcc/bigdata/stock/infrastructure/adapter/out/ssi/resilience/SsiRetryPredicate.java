package com.vcc.bigdata.stock.infrastructure.adapter.out.ssi.resilience;

import com.vcc.bigdata.stock.infrastructure.adapter.out.ssi.exception.SsiApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

/**
 * class filter the standard exception to retry
 */
@Slf4j
public class SsiRetryPredicate implements Predicate<Throwable> {

    private static final Set<Integer> RETRYABLE_STATUS_CODES = Set.of(
            HttpStatus.TOO_MANY_REQUESTS.value(),
            HttpStatus.BAD_GATEWAY.value(),
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            HttpStatus.GATEWAY_TIMEOUT.value()
    );

    @Override
    public boolean test(Throwable throwable) {
        if (throwable == null) {
            return false;
        }

        if (throwable instanceof ResourceAccessException || throwable instanceof IOException) {
            log.warn("[SSI-RETRY] Phát hiện lỗi kết nối/timeout mạng tới SSI: {}", throwable.getMessage());
            return true;
        }

        if (throwable instanceof HttpServerErrorException serverError) {
            log.warn("[SSI-RETRY] SSI Server trả về mã lỗi {}: {}",
                    serverError.getStatusCode(), serverError.getMessage());
            return true;
        }

        if (throwable instanceof HttpClientErrorException clientError) {
            HttpStatusCode status = clientError.getStatusCode();

            if (status.value() == HttpStatus.TOO_MANY_REQUESTS.value()) {
                log.warn("[SSI-RETRY] Bị SSI Rate Limit (HTTP 429). Kích hoạt cơ chế thử lại...");
                return true;
            }

            String responseBody = clientError.getResponseBodyAsString();
            if (isRateLimitText(responseBody)) {
                log.warn("[SSI-RETRY] Body phản hồi từ SSI chứa thông báo Rate Limit: {}", responseBody);
                return true;
            }

            log.error("[SSI-FAIL-FAST] Lỗi client không thể retry HTTP {}: {}", status, responseBody);
            return false;
        }

        if (throwable instanceof SsiApiException ssiEx) {
            if (RETRYABLE_STATUS_CODES.contains(ssiEx.getErrorCode()) || isRateLimitText(ssiEx.getRawResponseBody())) {
                log.warn("[SSI-RETRY] SSI trả mã lỗi nghiệp vụ {}: {}", ssiEx.getErrorCode(), ssiEx.getMessage());
                return true;
            }
            log.error("[SSI-FAIL-FAST] SSI trả mã lỗi không retry được: {}", ssiEx.getMessage());
            return false;
        }
        return false;
    }


    private boolean isRateLimitText(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }
        String lower = text.toLowerCase(Locale.ROOT);
        return lower.contains("rate limit")
                || lower.contains("too many requests")
                || lower.contains("quota exceeded")
                || lower.contains("qps limit")
                || lower.contains("429");
    }
}
