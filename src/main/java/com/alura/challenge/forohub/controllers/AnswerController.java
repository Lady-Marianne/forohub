package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.answer.*;
import com.alura.challenge.forohub.domain.topic.*;
import com.alura.challenge.forohub.domain.user.User;
import com.alura.challenge.forohub.infra.business.BusinessRulesService;
import com.alura.challenge.forohub.infra.exceptions.UnauthorizedActionException;
import com.alura.challenge.forohub.infra.exceptions.ValidationException;
import com.alura.challenge.forohub.domain.user.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
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

            Answer answer = answerRepository.getReferenceById(answerId); // Usar el ID del PathVariable.

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

    // Mostrar las respuestas a un tópico determinado por topicId:
    @GetMapping
    public ResponseEntity<List<DataResponseAnswer>> getAnswersByTopicId(@RequestParam Long topicId) {
        List<Answer> answers = topicRepository.findAnswersByTopicId(topicId);
        List<DataResponseAnswer> response = answers.stream()
                .map(DataResponseAnswer::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    // Cerrar un tópico:
    // Si un administrador marca, al menos una, de las respuestas como solución, cambiando el
    // campo 'solution' a 'true', el tópico se cierra automáticamente, cambiando su estado a 'CLOSED'.
    @PatchMapping("/{answerId}/solution")
    @Secured("ROLE_ADMIN")
    @Transactional
    public ResponseEntity<?> markAsSolution(@PathVariable Long answerId) {

        // Verificar si el usuario tiene el rol adecuado (ADMIN):
        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new UnauthorizedActionException("Usted no está autorizado a realizar esta acción.");
        }

        // Buscar la respuesta por su ID:
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada."));

        // Validar si ya está marcada como solución:
        if (answer.isSolution()) {
            throw new IllegalStateException("Esta respuesta ya está marcada como solución.");
        }
        // Marcar la respuesta como solución:
        answer.setSolution(true);

        // Cerrar el tópico relacionado usando el método de la clase Topic:
        Topic topic = answer.getOneTopic();
        if (!Status.CLOSED.equals(topic.getStatus())) {
            topic.closeTopic();
        }

        // Guardar los cambios:
        answerRepository.save(answer);
        topicRepository.save(topic);

        return ResponseEntity.ok("La respuesta fue marcada como solución y el tópico cerrado.");
    }

}