package com.alura.challenge.forohub.domain.topic;

import com.alura.challenge.forohub.domain.answer.Answer;
import com.alura.challenge.forohub.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Topic")
@Table(name = "topics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "topicId")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long topicId;
    private String title;
    private String message;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private String author;
    private String course;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Topic(DataRegisterTopic dataRegisterTopic, String username) {
        this.title = dataRegisterTopic.title();
        this.message = dataRegisterTopic.message();
        this.author = username;
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

