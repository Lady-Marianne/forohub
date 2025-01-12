package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.answer.*;
import com.alura.challenge.forohub.domain.topic.DataResponseTopic;
import com.alura.challenge.forohub.domain.topic.DataUpdateTopic;
import com.alura.challenge.forohub.domain.user.User;
import com.alura.challenge.forohub.infra.business.BusinessRulesService;
import com.alura.challenge.forohub.infra.exceptions.ValidationException;
import com.alura.challenge.forohub.domain.topic.Topic;
import com.alura.challenge.forohub.domain.topic.TopicRepository;
import com.alura.challenge.forohub.domain.user.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/answers")
@SecurityRequirement(name = "bearer-key")
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private BusinessRulesService businessRulesService;

    @Autowired
    private ResourceService resourceService;

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

        // Obtener usuario autenticado:
        User user = resourceService.getAuthenticatedUser();

        // Crear y guardar la respuesta:
        Answer answer = new Answer(
                dataRegisterAnswer,  // Usamos el DTO.
                user.getUsername(),  // El autor de la respuesta es el usuario logueado.
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

    // Actualizar una respuesta:
    @PutMapping("/{answerId}")
    @Transactional
    public ResponseEntity<DataResponseAnswer> updateAnswerById(@PathVariable Long answerId,
                                                             @RequestBody DataUpdateAnswer
                                                                     dataUpdateAnswer) {
        try {

            // Obtener el usuario autenticado:
            User user = resourceService.getAuthenticatedUser();

            Answer answer = answerRepository.getReferenceById(dataUpdateAnswer.answerId());

            // Validar que el usuario autenticado sea el autor del tópico:
            if (!answer.getAuthor().equals(user.getUsername())) {
                throw new SecurityException("No tiene permiso para editar esta respuesta.");
            }

            businessRulesService.validateEditTime(answer.getCreatedAt());

            answer.updateAnswer(dataUpdateAnswer);

            // Guardamos el tópico actualizado:
            answerRepository.save(answer);

            // Devolvemos la respuesta con el DTO actualizado:
            return ResponseEntity.ok(new DataResponseAnswer(answer));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("La respuesta con id " + answerId + " no existe.");
        }
    }

}
