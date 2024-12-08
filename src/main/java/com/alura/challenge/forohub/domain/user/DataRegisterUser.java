package com.alura.challenge.forohub.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DataRegisterUser(
        @NotBlank(message = "El nombre de usuario es obligatorio.")
        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres.")
        String username,

        @NotBlank(message = "El correo electrónico es obligatorio.")
        @Email(message = "El correo electrónico debe tener un formato válido.")
        String email,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
        String password
) {
}
