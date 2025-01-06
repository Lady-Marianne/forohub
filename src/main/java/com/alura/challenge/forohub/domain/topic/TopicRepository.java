package com.alura.challenge.forohub.domain.topic;

import com.alura.challenge.forohub.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("SELECT COUNT(t) > 0 FROM Topic t WHERE t.id = :topicId AND t.status = 'active'")
    boolean isTopicActive(@Param("topicId") Long topicId);

    Page<Topic> findByStatus(Status status, Pageable pageable);

    Optional<Topic> findByTitleAndMessage(String title, String message);

}
