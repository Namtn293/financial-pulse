package com.vcc.bigdata.stock.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String USER_ID_HEADER = "X-User-Id";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Stock Core Service API")
                        .description("REST API documentation for Stock Core Service.")
                        .version("0.0.1"))
                .addSecurityItem(new SecurityRequirement().addList(USER_ID_HEADER))
                .components(new Components()
                        .addSecuritySchemes(USER_ID_HEADER, new SecurityScheme()
                                .name(USER_ID_HEADER)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("User identity ID required for authentication")));
    }
}
