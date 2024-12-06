package com.alura.challenge.forohub.domain.topic;

/* Aclaración:
Este record llamado "DataResponseTopic" se refiere a la respuesta que recibirá
el cliente luego de postear un tópico.
No se refiera a las respuestas que puedan dar otros usuarios en ese tópico.
 */

public record DataResponseTopic(

        Long id,
        String title,
        String message,
        String createdAt,
        String status,
        String author,
        String course

) {

    public DataResponseTopic (Topic topic) {
        this
                (
                        topic.getId(),
                        topic.getTitle(),
                        topic.getMessage(),
                        topic.getCreatedAt().toString(),
                        topic.getStatus().toString(),
                        topic.getAuthor(),
                        topic.getCourse()
                );
    }

}
