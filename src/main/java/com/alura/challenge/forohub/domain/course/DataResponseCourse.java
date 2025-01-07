package com.alura.challenge.forohub.domain.course;

public record DataResponseCourse(

        Long courseId,
        String name,
        Category category // Aquí usamos el enum Category directamente.

) {
    public DataResponseCourse(Course course) {
        this(
                course.getCourseId(),
                course.getName(),
                course.getCategory() // Ya que category es un enum,
                // no necesitamos convertirlo a String, se devolverá como enum.
        );
    }
}
