package com.vcc.bigdata.stock.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(
            @Value("${app.ssi.api.connect-timeout-ms:5000}") int connectTimeout,
            @Value("${app.ssi.api.read-timeout-ms:15000}") int readTimeout
    ) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);

        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }
}
