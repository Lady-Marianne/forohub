package com.alura.challenge.forohub.domain.topic;

/* Aclaración:
Este record llamado "DataResponseTopic" se refiere a la respuesta que recibirá
el cliente luego de postear un tópico.
No se refiera a las respuestas que puedan dar otros usuarios en ese tópico.
 */

public record DataResponseTopic(

        Long topicId,
        String title,
        String message,
        String createdAt,
        String status,
        Long userId,
        String author,
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
                        topic.getCourse()
                );
    }

}
