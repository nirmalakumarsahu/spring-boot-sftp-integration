package com.sahu.springboot.basics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final OpenApiProperties openApiProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(openApiProperties.getTitle())
                        .description(openApiProperties.getDescription())
                        .version(openApiProperties.getVersion())
                        .contact(new Contact()
                                .name(openApiProperties.getContact().getName())
                                .email(openApiProperties.getContact().getEmail())
                                .url(openApiProperties.getContact().getUrl())
                        )
                )
                .servers(openApiProperties.getServers().stream().map(
                                server -> new Server().url(server.getUrl()).description(server.getDescription())
                        ).toList()
                );
    }

}
