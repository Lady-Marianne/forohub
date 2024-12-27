package com.alura.challenge.forohub.domain.answer;

import com.fasterxml.jackson.annotation.JsonFormat;

public record DataResponseAnswer(

            Long answerId,
            String message,
            Long topicId,
            String topic,

            @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
            String createdAt,

            Long authorId,
            String author,
            boolean solution
) {

    public DataResponseAnswer(Answer answer) {
        this
                (
                        answer.getAnswerId(),
                        answer.getMessage(),
                        answer.getOneTopic().getTopicId(),
                        answer.getTopic(),
                        answer.getCreatedAt().toString(),
                        answer.getUser().getUserId(),
                        answer.getAuthor(),
                        answer.isSolution()
                );
    }
}
