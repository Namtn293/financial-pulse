package com.ptit.bigdata.stockworker.application.port.out;

import com.ptit.bigdata.stockworker.domain.Stock;

public interface StockVietCadPort {
    Stock getDataFromVietCad(String symbol);
}
