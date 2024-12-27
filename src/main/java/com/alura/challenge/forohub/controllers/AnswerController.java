package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.answer.Answer;
import com.alura.challenge.forohub.domain.topic.DataResponseTopic;
import com.alura.challenge.forohub.domain.user.User;
import com.alura.challenge.forohub.infra.business.BusinessRulesService;
import com.alura.challenge.forohub.infra.exceptions.ValidationException;
import com.alura.challenge.forohub.domain.answer.AnswerRepository;
import com.alura.challenge.forohub.domain.answer.DataRegisterAnswer;
import com.alura.challenge.forohub.domain.answer.DataResponseAnswer;
import com.alura.challenge.forohub.domain.topic.Topic;
import com.alura.challenge.forohub.domain.topic.TopicRepository;
import com.alura.challenge.forohub.domain.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private BusinessRulesService businessRulesService;

    // Registrar nueva respuesta:

    @PostMapping
    public ResponseEntity<DataResponseAnswer> registerAnswer(
            @RequestBody @Valid DataRegisterAnswer dataRegisterAnswer,
            UriComponentsBuilder uriComponentsBuilder) {

        businessRulesService.validatePostingTime();



        // Verificar si el tópico existe en la base de datos,
        // buscando por el ID:
        Optional<Topic> existingTopic = topicRepository.findById(dataRegisterAnswer.topicId());
        if (existingTopic.isEmpty()) {
            throw new ValidationException("El tópico al que intentas responder no existe.");
        }
        // Verificar que el tópico esté activo:
        if (!topicRepository.isTopicActive(dataRegisterAnswer.topicId())) {
            throw new ValidationException("No se puede responder a un tópico que no está activo.");
        }

        // Obtener el usuario autenticado desde el contexto de seguridad:
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();

        // Buscar el usuario autenticado en la base de datos:
        User user = (User) userRepository.findByUsername(username);

// Crear y guardar la respuesta:
        Answer answer = new Answer(
                dataRegisterAnswer,  // Usamos el DTO.
                username,  // El autor de la respuesta es el usuario logueado.
                existingTopic.get()  // Aquí se pasa el tópico entero, no solo el título.
        );

        answer.setUser(user);  // Asignamos el usuario a la respuesta.
        answer.setOneTopic(existingTopic.get());  // Relacionamos la respuesta con el tópico.

        // Guardamos la respuesta en la base de datos:
        answer = answerRepository.save(answer);

        // Construir la URL del nuevo recurso:
        URI url = uriComponentsBuilder.path("/answers/{answerId}")
                .buildAndExpand(answer.getAnswerId()).toUri();

        // Retornar respuesta con el nuevo recurso creado:
        return ResponseEntity.created(url).body(new DataResponseAnswer(answer));
    }

}
