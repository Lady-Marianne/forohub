package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.user.DataAuthenticateUser;
import com.alura.challenge.forohub.domain.user.User;
import com.alura.challenge.forohub.infra.security.DataJWTToken;
import com.alura.challenge.forohub.infra.security.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Login de usuario (Authentication-Controller)")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity authenticateUser(@RequestBody @Valid
                                           DataAuthenticateUser dataAuthenticateUser) {

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                dataAuthenticateUser.username(),
                dataAuthenticateUser.password()
        );
        var authenticatedUser = authenticationManager.authenticate(authenticationToken);
        var JWTtoken = tokenService.generateToken((User) authenticatedUser.getPrincipal());
        return ResponseEntity.ok(new DataJWTToken(JWTtoken));
    }

}
