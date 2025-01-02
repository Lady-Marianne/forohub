package com.alura.challenge.forohub.infra.security;

import com.alura.challenge.forohub.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Obtener el token del header:
            var authHeader = request.getHeader("Authorization");
//        if (authHeader != null) {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                var token = authHeader.replace("Bearer ", "");
                var subject = tokenService.getSubject(token); // Extraer username.
                if (subject != null) {
                    // Token válido:
                    var user = userRepository.findByUsername(subject);
                    List<SimpleGrantedAuthority> roles = tokenService.getRoles(token).stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, roles);
                    user.getAuthorities(); // Forzamos un inicio de sesión.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Usuario autenticado: {}", subject);
                } else {
                    log.warn("Usuario no encontrado: {}", subject);
                }
            }
        } catch (Exception e) {
            // Manejo de errores:
            log.error("Error al procesar el token: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
        System.out.println("El filtro está siendo llamado.");
    }
}
