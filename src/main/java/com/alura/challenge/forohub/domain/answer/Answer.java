package com.alura.challenge.forohub.domain.answer;

import com.alura.challenge.forohub.domain.topic.Topic;
import com.alura.challenge.forohub.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "Answer")
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "answerId")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;
    private String message;
    private String topic;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    private String author;
    private boolean solution = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", referencedColumnName = "topic_id", nullable = false)
    private Topic oneTopic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    public Answer(DataRegisterAnswer dataRegisterAnswer, String username, Topic topic) {
        this.message = dataRegisterAnswer.message();
        this.topic = topic.getTitle(); // Usamos el título del tópico
        this.createdAt = LocalDateTime.now();
        this.author = username;
        this.solution = dataRegisterAnswer.solution(); // Si se recibe 'solution' en el DTO
        this.oneTopic = topic; // Asignamos el tópico a la respuesta.
    }

}
