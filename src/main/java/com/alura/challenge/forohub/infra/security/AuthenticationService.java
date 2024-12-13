package com.alura.challenge.forohub.infra.security;

import com.alura.challenge.forohub.domain.user.User;
import com.alura.challenge.forohub.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Buscando usuario: " + username);
        User user = (User) userRepository.findByUsername(username);
        System.out.println("Resultado de la búsqueda: " + user);

        if (user == null) {
//            System.out.println("Usuario no encontrado");
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        System.out.println("Usuario encontrado: " + user);
        return user;
    }
}
