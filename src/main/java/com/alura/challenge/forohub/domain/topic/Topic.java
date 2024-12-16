package com.alura.challenge.forohub.domain.topic;

import com.alura.challenge.forohub.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Topic")
@Table(name = "topics")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "topicId")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topicId")
    private Long topicId;
    private String title;
    private String message;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    private String author;
    private String course;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Topic(DataRegisterTopic dataRegisterTopic) {
        this.title = dataRegisterTopic.title();
        this.message = dataRegisterTopic.message();
        this.author = dataRegisterTopic.author();
        this.course = dataRegisterTopic.course();
        this.createdAt = LocalDateTime.now(); // Asigna la fecha actual.
        this.status = Status.PENDING; // Estado inicial.
    }

    public void updateTopic(DataUpdateTopic dataUpdateTopic) {
        if (dataUpdateTopic.title() != null)
            this.title = dataUpdateTopic.title();

        if (dataUpdateTopic.message() != null)
            this.message = dataUpdateTopic.message();
    }

    public void deleteTopic() {
        this.status = Status.DELETED;
    }

    public void approveTopic() {
        this.status = Status.ACTIVE;
    }
}
