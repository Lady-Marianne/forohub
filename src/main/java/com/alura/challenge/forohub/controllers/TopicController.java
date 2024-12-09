package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.ValidationException;
import com.alura.challenge.forohub.domain.topic.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    // Registrar tópico:
    @PostMapping
    public ResponseEntity<DataResponseTopic> registerTopic(
            @RequestBody @Valid DataRegisterTopic dataRegisterTopic,
            UriComponentsBuilder uriComponentsBuilder) {

        // Verificar si el tópico ya existe en la base de datos:
        Optional<Topic> existingTopic = topicRepository.findByTitleAndMessage(dataRegisterTopic.title(),
                dataRegisterTopic.message());

        // Si ya existe, lanzar una excepción:
        if (existingTopic.isPresent()) {
            throw new ValidationException("Ya existe un tópico con el mismo título y mensaje.");
        }
        // Crear y guardar el tópico:
        Topic topic = topicRepository.save(new Topic(dataRegisterTopic));

        // Construir URL del nuevo recurso:
        URI url = uriComponentsBuilder.path("/topics/{id}").buildAndExpand(topic.getId()).toUri();

        // Retornar respuesta utilizando el constructor del DTO:
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

    // Mostrar un tópico por id:
    @GetMapping("/{id}")
    public ResponseEntity returnTopicData(@PathVariable Long id) {
        try
        {Topic topic = topicRepository.getReferenceById(id);
            // Retornar respuesta utilizando el constructor del DTO:
            return ResponseEntity.ok(new DataResponseTopic(topic));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("El tópico con id " + id + " no existe.");
        }
    }

    // Actualizar un tópico:
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DataResponseTopic> updateTopicById(@PathVariable Long id,
                                                         @RequestBody DataUpdateTopic dataUpdateTopic) {
        try {
            Topic topic = topicRepository.getReferenceById(dataUpdateTopic.id());
            topic.updateTopic(dataUpdateTopic);
            // Guardamos el tópico actualizado:
            topicRepository.save(topic);
            // Devolvemos la respuesta con el DTO actualizado:
            return ResponseEntity.ok(new DataResponseTopic(topic));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("El tópico con id " + id + " no existe.");
        }
    }

    // Eliminar un tópico. Los tópicos que no cumplean con las reglas del foro, serán
    // eliminados de manera "lógica" por los administradores cambiando su status a
    // "DELETED" (eliminado):
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteTopicById(@PathVariable Long id) {
        Topic topic = topicRepository.getReferenceById(id);
        topic.deleteTopic();
        return ResponseEntity.noContent().build();
    }

    // Aprobar un tópico. Los tópicos tienen un status por default de "PENDING" (pendiente).
    // Si el tópico cumple con las reglas del foro, los administradores lo aprueban y cambian
    // su status a "ACTIVE" (activo):
    @PutMapping("/{id}/status")
    @Transactional
    public ResponseEntity approveTopicById(@PathVariable Long id) {
        Topic topic = topicRepository.getReferenceById(id);
        topic.approveTopic();
        return ResponseEntity.noContent().build();
    }
}

