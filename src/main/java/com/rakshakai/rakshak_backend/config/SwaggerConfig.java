package com.rakshakai.rakshak_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Rakshakai API Documentation")
                        .version("1.0.0")
                        .description("ai-powered citizen safety and legal assistant platform. " +
                                "This API provides endpoints for emergency case management, " +
                                "lawyer marketplace, and evidence storage.")
                        .contact(new Contact()
                                .name("Vishwanath")
                                .email("vishwanath@rakshakai.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server"),
                        new Server().url("https://api.rakshakai.com").description("Production Server (Future)")
                ));
    }
}

/*
 * After running the application, access Swagger UI at:
 * http://localhost:8080/swagger-ui/index.html
 *
 * API Documentation will be available at:
 * http://localhost:8080/v3/api-docs
 */