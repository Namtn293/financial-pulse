package com.vcc.bigdata.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StockCoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockCoreServiceApplication.class, args);
    }

}
