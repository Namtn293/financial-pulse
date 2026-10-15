package com.ptit.bigdata.stockworker.infrastructure.adapter.in.kafka.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventEnvelope<T> {
    private String eventId;
    private String eventType;
    private String source;
    private long timestamp;
    private String traceId;
    private String key;
    private T payload;
}
