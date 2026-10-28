package com.ptit.bigdata.stockworker.application.service;

import com.ptit.bigdata.stockworker.application.port.out.StockCachePort;
import com.ptit.bigdata.stockworker.application.port.out.StockRepositoryPort;
import com.ptit.bigdata.stockworker.application.port.out.StockVietCadPort;
import com.ptit.bigdata.stockworker.domain.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepositoryPort stockRepositoryPort;

    @Mock
    private StockCachePort stockCachePort;

    @Mock
    private StockVietCadPort stockVietCadPort;

    private StockService stockService;

    @BeforeEach
    void setUp() {
        stockService = new StockService(stockRepositoryPort, stockCachePort, stockVietCadPort);
    }

    @Test
    void testUpdateStock_success() {
        Stock stockVNM = Stock.builder().symbol("VNM").industry("Sữa").build();

        when(stockVietCadPort.getDataFromVietCad("VNM")).thenReturn(stockVNM);

        // Gọi hàm updateStock theo chuẩn 1:1 (nhận 1 symbol String)
        stockService.updateStock("VNM");

        verify(stockVietCadPort, times(1)).getDataFromVietCad("VNM");
        verify(stockRepositoryPort, times(1)).saveAll(anyCollection());
    }

    @Test
    void testUpdateStock_nullOrBlank() {
        stockService.updateStock("");
        stockService.updateStock("   ");
        stockService.updateStock(null);

        verify(stockVietCadPort, never()).getDataFromVietCad(anyString());
        verify(stockRepositoryPort, never()).saveAll(anyCollection());
    }

    @Test
    void testUpdateStock_notFoundOnVietCap() {
        // Khi VietCap trả về null (mã không tồn tại hoặc lỗi)
        when(stockVietCadPort.getDataFromVietCad("UNKNOWN")).thenReturn(null);

        stockService.updateStock("UNKNOWN");

        verify(stockVietCadPort, times(1)).getDataFromVietCad("UNKNOWN");
        // Không gọi lưu DB nếu stock là null
        verify(stockRepositoryPort, never()).saveAll(anyCollection());
    }
}

