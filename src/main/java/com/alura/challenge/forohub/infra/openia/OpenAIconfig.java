package com.alura.challenge.forohub.infra.openia;

import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIconfig {

    @Bean
    public OpenAiApi openAiApi() {
        // Obtener la clave API desde la variable de entorno
        String apiKey = System.getenv("OPENAI_API_KEY");

        // Verificar que la clave no sea null o vacía (esto es opcional pero recomendable):
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("La clave API de OpenAI no está configurada.");
        }

        // Crear y devolver la instancia de OpenAiApi:
        return new OpenAiApi(apiKey);
    }

}
