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
        publishEvents("STOCK_UPDATED", symbols);
    }

    @Override
    @Async
    public void publishCreatedStock(List<String> symbols) {
        publishEvents("STOCK_CREATED", symbols);
    }

    private void publishEvents(String eventType, List<String> symbols) {
        if (symbols == null || symbols.isEmpty()) {
            log.debug("No symbols to publish for eventType {}", eventType);
            return;
        }

        String currentTraceId = MDC.get("traceId") != null
                ? MDC.get("traceId")
                : UuidUtils.generateUuidV7().toString();

        for (String symbol : symbols) {
            EventEnvelope<String> eventEnvelope = EventEnvelope.<String>builder()
                    .source("stock-core-service")
                    .eventType(eventType)
                    .payload(symbol)
                    .traceId(currentTraceId)
                    .build();

            ProducerRecord<String, Object> record = new ProducerRecord<>(
                    stockUpdateTopic,
                    symbol,
                    eventEnvelope
            );

            record.headers().add(new RecordHeader("X-Event-Type", eventType.getBytes(StandardCharsets.UTF_8)));
            record.headers().add(new RecordHeader("X-Origin", "stock-core-service".getBytes(StandardCharsets.UTF_8)));

            long startTime = System.currentTimeMillis();
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(record);

            future.whenComplete((result, ex) -> {
               long duration = System.currentTimeMillis() - startTime;

                if (ex == null) {
                    log.info("Published event {} for symbol {} successfully. partition={}, offset={}, duration={}ms",
                            eventType, symbol,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset(),
                            duration);
                } else {
                    log.error("Failed to send Kafka message for symbol {} topic={}: {}", symbol, stockUpdateTopic, ex.getMessage(), ex);
                }
            });
        }
    }
}
