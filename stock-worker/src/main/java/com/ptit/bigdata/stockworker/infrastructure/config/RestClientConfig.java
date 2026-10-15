package com.ptit.bigdata.stockworker.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(
            @Value("${app.vietcad.api.connect-timeout-ms:5000}") int connectTimeout,
            @Value("${app.vietcad.api.read-timeout-ms:15000}") int readTimeout
    ) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(readTimeout);
        factory.setConnectTimeout(connectTimeout);

        ClientHttpRequestInterceptor gzipInterceptor = (request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            String contentEncoding = response.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING);
            if (contentEncoding != null && contentEncoding.toLowerCase().contains("gzip")) {
                return new ClientHttpResponse() {
                    @Override
                    public HttpStatusCode getStatusCode() throws IOException {
                        return response.getStatusCode();
                    }

                    @Override
                    public String getStatusText() throws IOException {
                        return response.getStatusText();
                    }

                    @Override
                    public void close() {
                        response.close();
                    }

                    @Override
                    public InputStream getBody() throws IOException {
                        return new GZIPInputStream(response.getBody());
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return response.getHeaders();
                    }
                };
            }
            return response;
        };

        return RestClient.builder()
                .requestFactory(factory)
                .requestInterceptor(gzipInterceptor)
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, identity")
                .build();
    }
}
