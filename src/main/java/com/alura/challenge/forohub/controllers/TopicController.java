package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.topic.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @PostMapping
    public ResponseEntity<DataResponseTopic> registerTopic(
            @RequestBody @Valid DataRegisterTopic dataRegisterTopic,
            UriComponentsBuilder uriComponentsBuilder) {

        // Crear y guardar el tópico:
        Topic topic = topicRepository.save(new Topic(dataRegisterTopic));

        // Construir URL del nuevo recurso:
        URI url = uriComponentsBuilder.path("/topics/{id}").buildAndExpand(topic.getId()).toUri();

        // Retornar respuesta utilizando el constructor del DTO:
        return ResponseEntity.created(url).body(new DataResponseTopic(topic));
    }

    @GetMapping
    public ResponseEntity<Page<DataResponseTopic>> listActiveTopics(@PageableDefault(size = 10)
                                                                        Pageable pageable) {
        Page<Topic> topics = topicRepository.findByStatus(Status.ACTIVE, pageable);
        Page<DataResponseTopic> response = topics.map(DataResponseTopic::new);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity returnTopicData(@PathVariable Long id) {
        Topic topic = topicRepository.getReferenceById(id);

        // Retornar respuesta utilizando el constructor del DTO:
        return ResponseEntity.ok(new DataResponseTopic(topic));
    }

}

