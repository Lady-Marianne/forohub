package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.ValidationException;
import com.alura.challenge.forohub.domain.topic.Topic;
import com.alura.challenge.forohub.domain.user.DataRegisterUser;
import com.alura.challenge.forohub.domain.user.DataResponseUser;
import com.alura.challenge.forohub.domain.user.User;
import com.alura.challenge.forohub.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<DataResponseUser> registerUser(
            @RequestBody @Validated DataRegisterUser dataRegisterUser,
            UriComponentsBuilder uriComponentsBuilder) {

        // Verificar si el usuario ya existe (por su correo electrónico o nombre de usuario):
        Optional<User> existingUser = userRepository.findByUsernameOrEmail(dataRegisterUser.username(),
                dataRegisterUser.eMail());

        // Si ya existe, lanzar una excepción:
        if (existingUser.isPresent()) {
            throw new ValidationException("El usuario ya está registrado.");
        }

        // Crear y guardar el usuario:
        User user = userRepository.save(new User(dataRegisterUser));
        // Construir URL del nuevo recurso:
        URI url = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();

        // Retornar respuesta utilizando el constructor del DTO:
        return ResponseEntity.created(url).body(new DataResponseUser(user));
    }
}
