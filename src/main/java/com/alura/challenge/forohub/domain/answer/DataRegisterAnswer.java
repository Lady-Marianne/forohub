package com.alura.challenge.forohub.domain.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DataRegisterAnswer(

        @NotNull
        Long topicId,

        @NotBlank
        String message

//        @NotNull
//        boolean solution

) {
}
