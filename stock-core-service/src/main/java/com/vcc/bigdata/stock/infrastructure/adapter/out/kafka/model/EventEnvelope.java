package com.vcc.bigdata.stock.infrastructure.adapter.out.kafka.model;

import com.vcc.bigdata.stock.infrastructure.util.UuidUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventEnvelope<T> {
    @Builder.Default
    private String eventId = UuidUtils.generateUuidV7().toString();

    private String eventType;

    private String source;

    @Builder.Default
    private long timestamp = System.currentTimeMillis();

    private String traceId;

    @Builder.Default
    private String key = null;

    private T payload;
}
