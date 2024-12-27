package com.alura.challenge.forohub.infra.business;

import com.alura.challenge.forohub.infra.exceptions.ValidationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class BusinessRulesService {

    public void validatePostingTime() {
        LocalTime now = LocalTime.now();
        LocalTime start = LocalTime.of(7, 0);
        LocalTime end = LocalTime.of(23, 59);

        if (now.isBefore(start) || now.isAfter(end)) {
            throw new ValidationException(
                    "Las publicaciones solo están permitidas entre las 7:00 y las 23:59.");
        }
    }

    public void validateEditTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        if (duration.toHours() > 1) {
            throw new ValidationException(
                    "Las ediciones sólo están permitidas dentro de la " +
                            "primera hora después de la publicación.");
        }
    }
}

