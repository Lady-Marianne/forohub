package com.alura.challenge.forohub.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("ForoHub API")
                        .description("API Rest de la aplicación ForoHub," +
                                " que gestiona tópicos y respuestas, " +
                                "permitiendo realizar operaciones CRUD " +
                                "y facilitar discusiones en línea.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo ForoHub")
                                .email("soporte@forohub.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://forohub.com/api/licencia")));

    }
}
