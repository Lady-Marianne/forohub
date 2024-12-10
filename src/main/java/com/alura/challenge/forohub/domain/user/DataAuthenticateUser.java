package com.alura.challenge.forohub.domain.user;

import jakarta.validation.constraints.NotNull;

public record DataAuthenticateUser(
        String username,
        String password
)
{

}
