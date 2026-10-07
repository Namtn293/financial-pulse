package com.vcc.bigdata.stock.infrastructure.adapter.out.kafka.config;

import com.fasterxml.jackson.databind.JsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import tools.jackson.databind.ser.jdk.StringSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.producer.client-id}")
    private String clientId;

    @Value("${kafka.producer.max-block-ms}")
    private int maxBlockMs;

    @Value("${kafka.producer.batch-size-bytes}")
    private int batchSizeBytes;

    @Value("${kafka.producer.linger-ms}")
    private int lingerMs;

    @Value("${kafka.producer.buffer-memory-bytes}")
    private long bufferMemoryBytes;

    @Value("${spring.kafka.producer.compression-type}")
    private String compressionType;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, maxBlockMs);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSizeBytes);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemoryBytes);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);

        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 5 * 1024 * 1024);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
