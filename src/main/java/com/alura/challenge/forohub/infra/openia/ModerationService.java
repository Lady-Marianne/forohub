package com.alura.challenge.forohub.infra.openia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModerationService {
    @Autowired
    private OpenAIService openAIService;

    public boolean moderateTopic(String title, String message) {
        String prompt = """
        Revisa si el siguiente contenido cumple con las reglas de un foro educativo:
        - Prohibidos mensajes discriminatorios, ofensivos o insultos.
        - Prohibidos temas irrelevantes para la temática del foro.
        Título: %s
        Mensaje: %s
        Responde con 'CUMPLE' o 'NO CUMPLE'.
        """.formatted(title, message);

        String response = openAIService.moderateContent(prompt);
        return response.equalsIgnoreCase("CUMPLE");
    }

}
