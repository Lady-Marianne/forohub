package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.course.Course;
import com.alura.challenge.forohub.domain.course.CourseRepository;
import com.alura.challenge.forohub.domain.course.DataRegisterCourse;
import com.alura.challenge.forohub.domain.course.DataResponseCourse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/courses")
@SecurityRequirement(name = "bearer-key")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden acceder a este endpoint.
    public ResponseEntity<DataResponseCourse> registerCourse
            (@RequestBody @Validated DataRegisterCourse dataRegisterCourse,
             UriComponentsBuilder uriComponentsBuilder) {
        // Crear el curso a partir de los datos recibidos:
        Course course = new Course(dataRegisterCourse);
        // Guardar el curso en la base de datos:
        courseRepository.save(course);
        // Construir URL del nuevo recurso:
        URI url = uriComponentsBuilder.path("/courses/{courseId}")
                .buildAndExpand(course.getCourseId()).toUri();
        // Devolver la respuesta con los detalles del curso creado:
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponseCourse(course));
    }
}
