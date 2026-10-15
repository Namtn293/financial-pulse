package com.vcc.bigdata.stock.infrastructure.adapter.out.kafka;

import com.vcc.bigdata.stock.application.port.out.StockKafkaPort;
import com.vcc.bigdata.stock.infrastructure.adapter.out.kafka.model.EventEnvelope;
import com.vcc.bigdata.stock.infrastructure.util.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaStockAdapter implements StockKafkaPort {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.stock-sync}")
    private String stockUpdateTopic;

    @Override
    @Async
    public void publishUpdatedStock(List<String> symbols) {
        if (symbols == null || symbols.isEmpty()){
            throw new IllegalArgumentException("Symbols partition key must not be null or empty!");
        }

        String currentTraceId = MDC.get("traceId") != null ? MDC.get("traceId") : UuidUtils.generateUuidV7().toString();
        EventEnvelope<Object> eventEnvelope = EventEnvelope.builder()
                .source("stock-core-service")
                .eventType("STOCKS_INFO_UPDATE")
                .traceId(currentTraceId)
                .payload(symbols)
                .build();

        ProducerRecord<String, Object> record = new ProducerRecord<>(
                stockUpdateTopic,
                eventEnvelope
        );

        record.headers().add(new RecordHeader("X-Event-Type", "UPDATE/CREATE".getBytes(StandardCharsets.UTF_8)));
        record.headers().add(new RecordHeader("X-Origin", "Stock-core-service".getBytes(StandardCharsets.UTF_8)));

        long startTime = System.currentTimeMillis();

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(record);

        future.whenComplete((result, ex) -> {
            long duration = System.currentTimeMillis() - startTime;

            if (ex == null) {
                log.info("Kafka publish SUCCESS: symbol={}, topic={}, partition={}, offset={}, timeTaken={}",
                        symbols,
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        duration);
            } else {
                log.error("Kafka publish FAILED: symbol={}, error={}, timeTaken={}ms",
                        symbols, ex.getMessage(), duration, ex);
                handleStockUpdateFallback(symbols, eventEnvelope);
            }
        });
    }

    private void handleStockUpdateFallback(List<String> symbols, EventEnvelope<?> eventEnvelope) {
        log.warn("Triggering Fallback Storage for symbol={}", symbols);
    }
}
