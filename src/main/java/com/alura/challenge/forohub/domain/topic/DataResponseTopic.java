package com.alura.challenge.forohub.domain.topic;

/* Aclaración:
Este record llamado "DataResponseTopic" se refiere a la respuesta que recibirá
el cliente luego de postear un tópico.
No se refiera a las respuestas que puedan dar otros usuarios en ese tópico.
 */

import com.fasterxml.jackson.annotation.JsonFormat;

public record DataResponseTopic(

        Long topicId,
        String title,
        String message,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        String createdAt,
        String status,
        Long userId,
        String author,
        Long courseId,
        String course

) {

    public DataResponseTopic (Topic topic) {
        this
                (
                        topic.getTopicId(),
                        topic.getTitle(),
                        topic.getMessage(),
                        topic.getCreatedAt().toString(),
                        topic.getStatus().toString(),
                        topic.getUser().getUserId(), // <-- Obtenemos el userId desde la entidad User.
                        topic.getAuthor(),
                        topic.getOneCourse().getCourseId(), // <-- Obtenemos el id desde la entidad Course.
                        topic.getOneCourse().getName()
                );
    }

}
