package com.alura.challenge.forohub.domain.answer;

import com.alura.challenge.forohub.domain.topic.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByTopic(Topic topic);
}
