package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.admin.Admin;
import com.alura.challenge.forohub.domain.user.*;
import com.alura.challenge.forohub.infra.exceptions.ValidationException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<DataResponseUser> registerUser(
            @RequestBody @Validated DataRegisterUser dataRegisterUser,
            UriComponentsBuilder uriComponentsBuilder) {
        return registerUserWithRole(dataRegisterUser, Role.USER, uriComponentsBuilder);
    }

    @PostMapping("/admins")
    public ResponseEntity<DataResponseUser> registerAdmin(
            @RequestBody @Validated DataRegisterUser dataRegisterUser,
            UriComponentsBuilder uriComponentsBuilder) {
        return registerUserWithRole(dataRegisterUser, Role.ADMIN, uriComponentsBuilder);
    }

    // Método privado genérico para registrar usuarios con cualquier rol:
    private ResponseEntity<DataResponseUser> registerUserWithRole(
            DataRegisterUser dataRegisterUser,
            Role role,
            UriComponentsBuilder uriComponentsBuilder) {

        // Verificar si el usuario ya existe (por su correo electrónico o nombre de usuario):
        Optional<User> existingUser = userRepository.findByUsernameOrEmail(dataRegisterUser.username(),
                dataRegisterUser.email());

        if (existingUser.isPresent()) {
            throw new ValidationException("El usuario ya está registrado.");
        }

        // Crear el usuario o admin con el rol especificado:
        User user;
        if (role == Role.ADMIN) {
            user = new Admin(dataRegisterUser);
        } else {
            user = new User(dataRegisterUser);
        }

        // Hashear la contraseña:
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        // Construir URL del nuevo recurso:
        URI url = uriComponentsBuilder.path("/users/{userId}").buildAndExpand(user.getUserId()).toUri();

        // Retornar respuesta utilizando el constructor del DTO:
        return ResponseEntity.created(url).body(new DataResponseUser(user));
    }
}
