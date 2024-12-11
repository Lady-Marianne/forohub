package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.user.DataAuthenticateUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity authenticateUser(@RequestBody @Valid
                                               DataAuthenticateUser dataAuthenticateUser) {

        Authentication token = new UsernamePasswordAuthenticationToken(
                dataAuthenticateUser.username(),
                dataAuthenticateUser.password()
        );
        authenticationManager.authenticate(token);

        return ResponseEntity.ok().build();
    }

}
