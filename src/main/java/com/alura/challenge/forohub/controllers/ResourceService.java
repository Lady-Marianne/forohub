package com.alura.challenge.forohub.controllers;

import com.alura.challenge.forohub.domain.topic.TopicRepository;
import com.alura.challenge.forohub.domain.user.User;
import com.alura.challenge.forohub.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    // Obtener el usuario autenticado desde el contexto de seguridad:
    public User getAuthenticatedUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        // Buscar el usuario autenticado en la base de datos:
        User user = (User) userRepository.findByUsername(username);
        return user;
    }
}
