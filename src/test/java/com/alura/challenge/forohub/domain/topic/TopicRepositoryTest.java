package com.alura.challenge.forohub.domain.topic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Test
    void isTopicActive() {
        // Preparar datos de prueba:
        DataRegisterTopic dataRegisterTopic = new DataRegisterTopic
                ("Título de prueba", "Mensaje de prueba", "Curso de prueba");
        Topic topic = new Topic(dataRegisterTopic, "usuarioPrueba");
        topic.setStatus(Status.ACTIVE);
        topicRepository.save(topic);

        // Ejecutar el método y verificar el resultado:
        boolean isActive = topicRepository.isTopicActive(topic.getTopicId());
        assertThat(isActive).isTrue();
    }

    @Test
    void findByStatus() {
        // Preparar datos de prueba:
        DataRegisterTopic dataRegisterTopic1 = new DataRegisterTopic
                ("Título 1", "Mensaje 1", "Curso 1");
        Topic topic1 = new Topic(dataRegisterTopic1, "usuario1");
        topic1.setStatus(Status.ACTIVE);
        topicRepository.save(topic1);

        DataRegisterTopic dataRegisterTopic2 = new DataRegisterTopic
                ("Título 2", "Mensaje 2", "Curso 2");
        Topic topic2 = new Topic(dataRegisterTopic2, "usuario2");
        topic2.setStatus(Status.PENDING);
        topicRepository.save(topic2);

        // Ejecutar el método y verificar el resultado:
        var pageable = PageRequest.of(0, 10);
        var activeTopics = topicRepository.findByStatus(Status.ACTIVE, pageable);
        assertThat(activeTopics.getContent()).contains(topic1);
        assertThat(activeTopics.getContent()).doesNotContain(topic2);
    }

    @Test
    void findByTitleAndMessage() {
        // Preparar datos de prueba:
        DataRegisterTopic dataRegisterTopic = new DataRegisterTopic
                ("Título único", "Mensaje único", "Curso único");
        Topic topic = new Topic(dataRegisterTopic, "usuarioUnico");
        topicRepository.save(topic);

        // Ejecutar el método y verificar el resultado:
        Optional<Topic> foundTopic = topicRepository.findByTitleAndMessage
                ("Título único", "Mensaje único");
        assertThat(foundTopic).isPresent();
        assertThat(foundTopic.get()).isEqualTo(topic);
    }
}