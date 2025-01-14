package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.course.Course;
import com.alura.challenge.forohub.domain.course.CourseRepository;
import com.alura.challenge.forohub.domain.course.DataRegisterCourse;
import com.alura.challenge.forohub.domain.course.DataResponseCourse;
import com.alura.challenge.forohub.infra.exceptions.ValidationException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.web.PageableDefault;

import java.net.URI;
import java.util.Optional;

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
        // Verificar si el curso ya existe en la base de datos, buscando
        // por nombre:
        Optional<Course> existingCourse = courseRepository.findByName(dataRegisterCourse.name());
        if (existingCourse.isPresent()) {
            throw new ValidationException("Ya existe ese curso en la base de datos.");
        }
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

    // Listar todos los cursos con paginación:
    @GetMapping
    public ResponseEntity<Page<DataResponseCourse>> listAllCourses(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);
        Page<DataResponseCourse> response = courses.map(DataResponseCourse::new);
        return ResponseEntity.ok(response);
    }
}
