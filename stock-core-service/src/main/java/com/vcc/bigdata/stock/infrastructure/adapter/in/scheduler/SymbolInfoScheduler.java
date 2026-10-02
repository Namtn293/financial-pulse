package com.vcc.bigdata.stock.infrastructure.adapter.in.scheduler;

import com.vcc.bigdata.stock.application.port.in.StockUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SymbolInfoScheduler {
    private final StockUseCase stockUseCase;

    @Scheduled(cron = "${app.scheduler.ssi.cron:0 0 3 * * 7}")
    @SchedulerLock(
            name = "SymbolInfoScheduler",
            lockAtLeastFor = "1m",
            lockAtMostFor = "15m"
    )
    public void syncSymbolInfo() {
        try {
            stockUseCase.sync();
        } catch (Exception e){
            log.error("Unhandled error in scheduled sync new symbol stock info {}" , e.getMessage(),e);
        }
    }
}
