package com.alura.challenge.forohub.infra.openia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModerationService {
    @Autowired
    private OpenAIService openAIService;

    public boolean moderateTopic(String title, String message) {
        String prompt = """
        Revisa si el siguiente contenido cumple con las reglas del foro educativo "ForoHub":
        Reglas:
        1. Prohibido contenido discriminatorio, ofensivo o insultante.
        Ejemplo de mensaje discriminatorio: "Las mujeres no deberían programar".
        2. Prohibido contenido irrelevante para la temática del foro.
        Temática del foro:
        - Desarrollo backend y frontend.
        - DevOps.
        - Bases de datos.
        - Ingeniería de software.
        Ejemplo de tema irrelevante: "No puedo conectar mi impresora".
        Contenido a evaluar:
        Título: %s
        Mensaje: %s
        Responde únicamente con 'CUMPLE' o 'NO CUMPLE'.
        """.formatted(title, message);

        String response = openAIService.moderateContent(prompt);
        System.out.println("Respuesta de OpenAI: " + response);
        return response.trim().equalsIgnoreCase("CUMPLE");
    }
}
