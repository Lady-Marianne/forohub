package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.course.Course;
import com.alura.challenge.forohub.domain.course.CourseRepository;
import com.alura.challenge.forohub.infra.business.BusinessRulesService;
import com.alura.challenge.forohub.infra.exceptions.UnauthorizedActionException;
import com.alura.challenge.forohub.infra.exceptions.ValidationException;
import com.alura.challenge.forohub.domain.topic.*;
import com.alura.challenge.forohub.domain.user.User;
import com.alura.challenge.forohub.domain.user.UserRepository;
import com.alura.challenge.forohub.infra.openia.ModerationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topics")
@SecurityRequirement(name = "bearer-key")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private BusinessRulesService businessRulesService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ModerationService moderationService;

    // Registrar nuevo tópico:
    @PostMapping
    public ResponseEntity<DataResponseTopic> registerTopic(
            @RequestBody @Valid DataRegisterTopic dataRegisterTopic,
            UriComponentsBuilder uriComponentsBuilder) {

        businessRulesService.validatePostingTime();

        // Verificar si el tópico ya existe en la base de datos, buscando
        // por título y mensaje:
        Optional<Topic> existingTopic = topicRepository.findByTitleAndMessage(dataRegisterTopic.title(),
                dataRegisterTopic.message());

        if (existingTopic.isPresent()) {
            throw new ValidationException("Ya existe un tópico con el mismo título y mensaje.");
        }

        // Buscar el curso por ID:
        Course course = courseRepository.findById(dataRegisterTopic.courseId())
                .orElseThrow(() -> new ValidationException("El curso especificado no existe."));

        // Obtener usuario autenticado:
        User user = resourceService.getAuthenticatedUser();

        // Crear el tópico:
        Topic topic = new Topic(dataRegisterTopic, user.getUsername());
        topic.setUser(user); // Asigna el usuario autenticado al tópico.
        topic.setOneCourse(course);

        // Asignar el nombre del curso al campo 'course' (String):
        topic.setCourse(course.getName());

        // Llamar a la moderación del contenido:
        boolean isValid = moderationService.moderateTopic(dataRegisterTopic.title(),
                dataRegisterTopic.message());
        if (isValid) {
            topic.approveTopic(); // Cambiar estado a "ACTIVE".
        } else {
            topic.deleteTopic(); // Cambiar estado a "DELETED".
        }
        System.out.println("Resultado de la moderación: " + isValid);
        topic = topicRepository.save(topic);

        // Construir URL del nuevo recurso:
        URI url = uriComponentsBuilder.path("/topics/{topicId}")
                .buildAndExpand(topic.getTopicId()).toUri();

        return ResponseEntity.created(url).body(new DataResponseTopic(topic));
    }

    // Mostrar los tópicos según su status. Sólo los administradores pueden
    // ver los tópicos con estado "DELETED" o "PENDING".
    // El resto puede ser visto por todos los usuarios.
    // Endpoints:
    // - GET /topics
    // - GET /topics?status=CLOSED
    // - GET /topics?status=ARCHIVED
    // - GET /topics?status=DELETED
    // - GET /topics?status=PENDING
    @GetMapping
    public ResponseEntity<Page<DataResponseTopic>> listTopicsByStatus(
            @RequestParam(required = false, defaultValue = "ACTIVE") String status,
            @PageableDefault(size = 10)
            Pageable pageable) {

        // Convertir el parámetro `status` a un valor del Enum `Status`:
        Status topicStatus;
        try {
            topicStatus = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("El estado proporcionado no es válido." +
                    " Use ACTIVE, PENDING, CLOSED, ARCHIVED o DELETED.");
        }

        // Verificar si el estado solicitado es PENDING o DELETED
        if ((topicStatus == Status.PENDING || topicStatus == Status.DELETED) &&
                !resourceService.isAdmin()) {
            throw new AccessDeniedException("No tiene permisos para ver tópicos " +
                    "borrados o pendientes de moderación.");
        }
        // Filtrar los tópicos por el estado solicitado:
        Page<Topic> topics = topicRepository.findByStatus(topicStatus, pageable);
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

            // Obtener el usuario autenticado:
            User user = resourceService.getAuthenticatedUser();

            Topic topic = topicRepository.getReferenceById(topicId); // Usar el ID del PathVariable.

            // Validar que el usuario autenticado sea el autor del tópico:
            if (!topic.getAuthor().equals(user.getUsername())) {
                throw new SecurityException("No tiene permiso para editar este tópico.");
            }

            businessRulesService.validateEditTime(topic.getCreatedAt());

            topic.updateTopic(dataUpdateTopic);

            // Guardamos el tópico actualizado:
            topicRepository.save(topic);

            // Devolvemos la respuesta con el DTO actualizado:
            return ResponseEntity.ok(new DataResponseTopic(topic));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("El tópico con id " + topicId + " no existe.");
        }
    }

    // Eliminar un tópico. Los tópicos que no cumplen con las reglas del foro, serán
    // eliminados de manera "lógica" por los administradores cambiando su status a
    // "DELETED" (eliminado):
    @DeleteMapping("/{topicId}")
    @Secured("ROLE_ADMIN")
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
    @Secured("ROLE_ADMIN")
    @Transactional
    public ResponseEntity approveTopicById(@PathVariable Long topicId) {
        Topic topic = topicRepository.getReferenceById(topicId);
        topic.approveTopic();
        return ResponseEntity.noContent().build();
    }
}

