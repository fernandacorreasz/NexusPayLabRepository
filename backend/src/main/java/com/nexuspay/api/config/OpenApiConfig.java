package com.nexuspay.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  OpenAPI nexusOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("NexusPay API").version("1.0.0").description("NexusPay digital banking platform API"))
        .components(new Components().addSecuritySchemes("basicAuth",
            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")));
  }
}
