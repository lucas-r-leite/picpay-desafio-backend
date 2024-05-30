package com.example.desafio_backend.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Desafio PicPay")
                        .description("Desafio PicPay")
                        .version("1.0.0")
                );
    }

    @Bean
    public GroupedOpenApi user() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/api/users")
                .build();
    }

    @Bean
    public GroupedOpenApi signup() {
        return GroupedOpenApi.builder()
                .group("signup")
                .pathsToMatch("/api/signup")
                .build();
    }

    @Bean
    public GroupedOpenApi wallet() {
        return GroupedOpenApi.builder()
                .group("wallet")
                .pathsToMatch("/api/wallet/**")
                .build();
    }

}
