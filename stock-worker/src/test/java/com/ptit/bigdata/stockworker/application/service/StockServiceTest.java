package com.ptit.bigdata.stockworker.application.service;

import com.ptit.bigdata.stockworker.application.port.out.StockVietCadPort;
import com.ptit.bigdata.stockworker.domain.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockVietCadPort stockVietCadPort;

    private StockService stockService;



    @BeforeEach
    void setUp() {
        stockService = new StockService(stockVietCadPort);
    }

    @Test
    void testUpdateStock_success() {
        Stock stockVNM = Stock.builder().symbol("VNM").build();
        Stock stockFPT = Stock.builder().symbol("FPT").build();

        when(stockVietCadPort.getDataFromVietCad("VNM")).thenReturn(stockVNM);
        when(stockVietCadPort.getDataFromVietCad("FPT")).thenReturn(stockFPT);

        stockService.updateStock(List.of("VNM", "FPT"));

        verify(stockVietCadPort, times(1)).getDataFromVietCad("VNM");
        verify(stockVietCadPort, times(1)).getDataFromVietCad("FPT");
    }

    @Test
    void testUpdateStock_emptyList() {
        stockService.updateStock(List.of());
        verify(stockVietCadPort, never()).getDataFromVietCad(anyString());
    }
}
