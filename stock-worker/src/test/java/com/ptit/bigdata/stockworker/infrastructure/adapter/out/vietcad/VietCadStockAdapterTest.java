package com.ptit.bigdata.stockworker.infrastructure.adapter.out.vietcad;

import com.ptit.bigdata.stockworker.domain.Stock;
import com.ptit.bigdata.stockworker.infrastructure.adapter.out.vietcad.mapper.StockVietCapMapper;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class VietCadStockAdapterTest {

    @Mock
    private RestClient restClient;

    @Mock
    private StockVietCapMapper stockVietCapMapper;

    private VietCadStockAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new VietCadStockAdapter(restClient, stockVietCapMapper);
    }

    @Test
    void testGetDataFromVietCad_blankSymbol_returnsNull() {
        assertNull(adapter.getDataFromVietCad(""));
        assertNull(adapter.getDataFromVietCad(null));
    }

    @Test
    void testGetDataFromVietCadFallback_rateLimitExceeded() {
        RequestNotPermitted ex = mock(RequestNotPermitted.class);
        assertThrows(RuntimeException.class, () -> adapter.getDataFromVietCadFallback("VNM", ex));
    }

    @Test
    void testGetDataFromVietCadFallback_genericException() {
        assertThrows(RuntimeException.class, () ->
                adapter.getDataFromVietCadFallback("VNM", new RuntimeException("API down")));
    }
}
