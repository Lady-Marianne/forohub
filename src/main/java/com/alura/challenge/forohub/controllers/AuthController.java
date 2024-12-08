package com.alura.challenge.forohub.controllers;

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
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
        // Crear un token de autenticación con las credenciales
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword());

        // Autenticar al usuario
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // Establecer el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Si llegamos aquí, el usuario ha sido autenticado correctamente
        return ResponseEntity.ok("Usuario autenticado con éxito");
    }
}
