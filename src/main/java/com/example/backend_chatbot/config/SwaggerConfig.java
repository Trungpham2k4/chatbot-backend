package com.example.backend_chatbot.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private final String[] matching = {"/api/**", "/user/**", "/auth/**"};

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch(matching) // Các đường dẫn API sẽ được Swagger scan
                .build();
    }
}
