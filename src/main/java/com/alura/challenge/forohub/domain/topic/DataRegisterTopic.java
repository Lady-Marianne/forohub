package com.alura.challenge.forohub.domain.topic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DataRegisterTopic(

        @NotNull
        Long courseId,
        @NotBlank
        String title,
        @NotBlank
        String message


) {
}
