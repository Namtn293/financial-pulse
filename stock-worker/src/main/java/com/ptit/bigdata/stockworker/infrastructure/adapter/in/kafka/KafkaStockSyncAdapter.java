package com.ptit.bigdata.stockworker.infrastructure.adapter.in.kafka;

import com.ptit.bigdata.stockworker.application.port.in.StockUseCase;
import com.ptit.bigdata.stockworker.infrastructure.adapter.in.kafka.dto.EventEnvelope;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class KafkaStockSyncAdapter {
    private final StockUseCase stockUseCase;

    public KafkaStockSyncAdapter(StockUseCase stockUseCase) {
        this.stockUseCase = stockUseCase;
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.stock-sync}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "singleKafkaListenerContainerFactory"
    )
    public void onMessage(
            ConsumerRecord<String, EventEnvelope<?>> record,
            Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) int offset
    ) {
        log.info("Received Kafka message: partition={}, offset={}, payload={}", partition, offset, record.value().getPayload().toString());
        List<String> symbols = Arrays.stream(record.value().getPayload().toString()
                .replace("[","")
                .replace("]","").trim()
                .split(", ")).toList();

//        stockUseCase.updateStock(symbols);

        if (acknowledgment != null) {
            acknowledgment.acknowledge();
        }
    }
}
