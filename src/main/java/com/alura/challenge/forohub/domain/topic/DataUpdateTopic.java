package com.alura.challenge.forohub.domain.topic;

import jakarta.validation.constraints.NotNull;

public record DataUpdateTopic(
        Long id,
        String title,
        String message
) {
}
