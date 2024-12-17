package com.alura.challenge.forohub.domain.topic;
import jakarta.validation.constraints.NotBlank;

public record DataRegisterTopic(

        @NotBlank
        String title,
        @NotBlank
        String message,
        @NotBlank
        String course
) {
}
