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
            ConsumerRecord<String, EventEnvelope<String>> record,
            Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) int offset
    ) {
        EventEnvelope<String> envelope = record.value();
        if (envelope == null || envelope.getPayload() == null) {
            log.warn("Received empty Kafka record at partition={}, offset={}. Skipping.", partition, offset);
            if (acknowledgment != null) acknowledgment.acknowledge();
            return;
        }

        String symbol = envelope.getPayload().trim().toUpperCase();
        log.info("Processing Kafka event '{}' from '{}', symbol={}, traceId={}, partition={}, offset={}",
                envelope.getEventType(), envelope.getSource(), symbol, envelope.getTraceId(), partition, offset);

        try {
            stockUseCase.updateStock(symbol);

            if (acknowledgment != null) {
                acknowledgment.acknowledge();
            }
        } catch (Exception ex) {
            log.error("Failed to process Kafka stock sync event for symbol {} at partition={}, offset={}. Triggering Kafka retry! Error: {}",
                    symbol, partition, offset, ex.getMessage());
            throw ex;
        }
    }
}
