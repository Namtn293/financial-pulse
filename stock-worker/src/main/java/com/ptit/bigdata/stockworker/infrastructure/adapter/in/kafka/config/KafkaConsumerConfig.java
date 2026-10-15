package com.ptit.bigdata.stockworker.infrastructure.adapter.in.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.backoff.FixedBackOff;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.concurrency}")
    private int concurrency;

    @Value("${spring.kafka.consumer.max-pool-records}")
    private int maxPoolRecords;

    @Value("${spring.kafka.consumer.max-poll-interval-ms}")
    private int maxPollIntervalMs;

    @Value("${spring.kafka.consumer.session-timeout-ms}")
    private int sessionTimeoutMs;

    @Value("${spring.kafka.consumer.heartbeat-interval-ms}")
    private int heartbeatIntervalMs;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Bean
    public ConsumerFactory<String, Object> stringObjectConsumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPoolRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatIntervalMs);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class.getName());

        props.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JacksonJsonDeserializer.TYPE_MAPPINGS,
                "com.vcc.bigdata.stock.infrastructure.adapter.out.kafka.model.EventEnvelope:com.ptit.bigdata.stockworker.infrastructure.adapter.in.kafka.dto.EventEnvelope");
        props.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, "com.ptit.bigdata.stockworker.infrastructure.adapter.in.kafka.dto.EventEnvelope");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    // bean for solving single message
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> singleKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringObjectConsumerFactory());
        factory.setConcurrency(concurrency);

        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("kafka-listener-");
        executor.setVirtualThreads(true);
        factory.getContainerProperties().setListenerTaskExecutor(executor);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(1000L, 3L));
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    // bean for solving batch messages
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> batchKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringObjectConsumerFactory());
        factory.setConcurrency(concurrency);

        factory.setBatchListener(true);

        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("kafka-listener-");
        executor.setVirtualThreads(true);
        factory.getContainerProperties().setListenerTaskExecutor(executor);

        return factory;
    }
}
