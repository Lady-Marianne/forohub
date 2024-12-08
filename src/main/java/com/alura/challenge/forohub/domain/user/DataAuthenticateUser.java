package com.alura.challenge.forohub.domain.user;

import jakarta.validation.constraints.NotNull;

public class DataAuthenticateUser {

    @NotNull
    private String username;

    @NotNull
    private String password;

}
