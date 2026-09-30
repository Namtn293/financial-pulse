package com.vcc.bigdata.stock.application.port.out;

public interface WatchListRepositoryPort {
    boolean existByUserIdAndName(Long userId, String name);


}
