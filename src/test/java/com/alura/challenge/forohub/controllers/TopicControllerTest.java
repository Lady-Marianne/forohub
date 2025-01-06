package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.topic.DataRegisterTopic;
import com.alura.challenge.forohub.domain.topic.DataResponseTopic;
import com.alura.challenge.forohub.domain.topic.Topic;
import com.alura.challenge.forohub.domain.topic.TopicRepository;
import com.alura.challenge.forohub.domain.user.User;
import com.alura.challenge.forohub.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TopicControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DataRegisterTopic> dataRegisterTopicJson;

    @Autowired
    private JacksonTester<DataResponseTopic> dataResponseTopicJson;

    @Autowired
    private ResourceService resourceService;

    @MockitoBean
    private TopicRepository topicRepository;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @WithMockUser
    @DisplayName("Debería devolver HTTP 400 si el registro no tiene datos válidos.")
    void registerTopic_invalidData() throws Exception {
        var response = mvc.perform(post("/topics"))
                .andReturn()
                .getResponse();
        assertThat(response
                .getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Debería devolver código http 201 cuando la información es válida.")
    @WithMockUser
    void registerTopic_validData() throws Exception {
        var dataRegisterTopic = new DataRegisterTopic(
                "Título de prueba",
                "Mensaje de prueba",
                "Curso de prueba");

        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setUsername("usuarioPrueba");

        Topic savedTopic = new Topic(dataRegisterTopic, mockUser.getUsername());
        savedTopic.setTopicId(1L);
        savedTopic.setUser(mockUser);

        when(topicRepository.save(any())).thenReturn(savedTopic);
        when(resourceService.getAuthenticatedUser()).thenReturn(mockUser);

        var response = mvc
                .perform(post("/topics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataRegisterTopicJson.write(dataRegisterTopic).getJson()))
                .andReturn().getResponse();

        var jsonExpected = dataResponseTopicJson.write(
                new DataResponseTopic(
                        savedTopic.getTopicId(),
                        savedTopic.getTitle(),
                        savedTopic.getMessage(),
                        savedTopic.getCreatedAt().toString(),
                        savedTopic.getStatus().toString(),
                        savedTopic.getUser().getUserId(),
                        savedTopic.getAuthor(),
                        savedTopic.getCourse()
                )
        ).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonExpected);
    }

}