package com.alura.challenge.forohub.domain.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DataRegisterCourse(

            @NotBlank
            String name,

            @NotNull
            Category category

) {
}
