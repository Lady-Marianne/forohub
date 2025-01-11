package com.alura.challenge.forohub.infra.openia;

import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAIService {

    private final OpenAiApi openAiApi;

    // Constructor para inyectar OpenAiApi:
    public OpenAIService(OpenAiApi openAiApi) {
        this.openAiApi = openAiApi;
    }

    public String moderateContent(String prompt) {

        // Crear el mensaje de entrada:
        OpenAiApi.ChatCompletionMessage chatCompletionMessage =
                new OpenAiApi.ChatCompletionMessage(prompt,
                OpenAiApi.ChatCompletionMessage.Role.USER);

        // Crear la solicitud a OpenAI con el modelo y los parámetros:
        OpenAiApi.ChatCompletionRequest request = new OpenAiApi.ChatCompletionRequest(
                List.of(chatCompletionMessage),
                "gpt-4o-mini",
                0.7,  // Temperatura
                false // Si no es una solicitud de transmisión.
        );

        // Realizar la solicitud sincrónica:
        ResponseEntity<OpenAiApi.ChatCompletion> response = openAiApi.chatCompletionEntity(request);

        // Obtener la respuesta:
        OpenAiApi.ChatCompletion chatCompletion = response.getBody();

        // Verificar si la respuesta es válida:
        if (chatCompletion != null) {
            // Aquí exploramos la respuesta para ver qué contiene.
            // Imprimimos el contenido de la respuesta para inspeccionarlo:
            System.out.println("Respuesta completa: " + chatCompletion.toString());
            return chatCompletion.toString();  // Ver qué contiene la respuesta completa.
        } else {
            return "Error: No se recibió respuesta válida.";
        }

    }
}