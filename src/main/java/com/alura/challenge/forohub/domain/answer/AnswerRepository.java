package com.alura.challenge.forohub.domain.answer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
//    Page<Answer> findByOneTopic_TopicId(Long topicId, Pageable pageable);
}
