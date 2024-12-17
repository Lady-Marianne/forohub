package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.ValidationException;
import com.alura.challenge.forohub.domain.topic.*;
import com.alura.challenge.forohub.domain.user.User;
import com.alura.challenge.forohub.domain.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    // Registrar nuevo tópico:

    @PostMapping
    public ResponseEntity<DataResponseTopic> registerTopic(
            @RequestBody @Valid DataRegisterTopic dataRegisterTopic,
            UriComponentsBuilder uriComponentsBuilder) {

        // Verificar si el tópico ya existe en la base de datos
        Optional<Topic> existingTopic = topicRepository.findByTitleAndMessage(dataRegisterTopic.title(),
                dataRegisterTopic.message());

        if (existingTopic.isPresent()) {
            throw new ValidationException("Ya existe un tópico con el mismo título y mensaje.");
        }

        // Obtener el usuario autenticado desde el contexto de seguridad:
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();

        // Buscar el usario autenticado en la base de datos:
        User user = (User) userRepository.findByUsername(username);

        // Crear y guardar el tópico:
        Topic topic = new Topic(dataRegisterTopic, username);
        topic.setUser(user); // Asigna el usuario autenticado al tópico.
        topic = topicRepository.save(topic);

        // Construir URL del nuevo recurso:
        URI url = uriComponentsBuilder.path("/topics/{topicId}")
                .buildAndExpand(topic.getTopicId()).toUri();

        return ResponseEntity.created(url).body(new DataResponseTopic(topic));
    }

    // Mostrar los tópicos activos:
    @GetMapping
    public ResponseEntity<Page<DataResponseTopic>> listActiveTopics(@PageableDefault(size = 10)
                                                                    Pageable pageable) {
        Page<Topic> topics = topicRepository.findByStatus(Status.ACTIVE, pageable);
        Page<DataResponseTopic> response = topics.map(DataResponseTopic::new);
        return ResponseEntity.ok(response);
    }

    // Mostrar un tópico por topicId:
    @GetMapping("/{topicId}")
    public ResponseEntity returnTopicData(@PathVariable Long topicId) {
        try
        {Topic topic = topicRepository.getReferenceById(topicId);
            // Retornar respuesta utilizando el constructor del DTO:
            return ResponseEntity.ok(new DataResponseTopic(topic));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("El tópico con id " + topicId + " no existe.");
        }
    }

    // Actualizar un tópico:
    @PutMapping("/{topicId}")
    @Transactional
    public ResponseEntity<DataResponseTopic> updateTopicById(@PathVariable Long topicId,
                                                         @RequestBody DataUpdateTopic dataUpdateTopic) {
        try {
            Topic topic = topicRepository.getReferenceById(dataUpdateTopic.topicId());
            topic.updateTopic(dataUpdateTopic);
            // Guardamos el tópico actualizado:
            topicRepository.save(topic);
            // Devolvemos la respuesta con el DTO actualizado:
            return ResponseEntity.ok(new DataResponseTopic(topic));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("El tópico con id " + topicId + " no existe.");
        }
    }

    // Eliminar un tópico. Los tópicos que no cumplean con las reglas del foro, serán
    // eliminados de manera "lógica" por los administradores cambiando su status a
    // "DELETED" (eliminado):
    @DeleteMapping("/{topicId}")
    @Transactional
    public ResponseEntity deleteTopicById(@PathVariable Long topicId) {
        Topic topic = topicRepository.getReferenceById(topicId);
        topic.deleteTopic();
        return ResponseEntity.noContent().build();
    }

    // Aprobar un tópico. Los tópicos tienen un status por default de "PENDING" (pendiente).
    // Si el tópico cumple con las reglas del foro, los administradores lo aprueban y cambian
    // su status a "ACTIVE" (activo):
    @PutMapping("/{topicId}/status")
    @Transactional
    public ResponseEntity approveTopicById(@PathVariable Long topicId) {
        Topic topic = topicRepository.getReferenceById(topicId);
        topic.approveTopic();
        return ResponseEntity.noContent().build();
    }
}

