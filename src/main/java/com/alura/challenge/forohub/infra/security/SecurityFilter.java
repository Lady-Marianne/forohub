package com.alura.challenge.forohub.infra.security;

import com.alura.challenge.forohub.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Obtener el token del header:
        var authHeader = request.getHeader("Authorization");

//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            // Detenemos la cadena y lanzamos un error si no hay token:
//            response.setContentType("application/json");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("{\"error\": \"Token no proporcionado o inválido.\"}");
//            return;
//        }

        if (authHeader != null) {
        var token = authHeader.replace("Bearer ", "");
        var subject = tokenService.getSubject(token); // Extraer username.
        if (subject != null) {
            // Token válido:
            var user = userRepository.findByUsername(subject);
            var authentication = new UsernamePasswordAuthenticationToken(user, null,
                    user.getAuthorities()); // Forzamos un inicio de sesión.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        }
        filterChain.doFilter(request, response);
        System.out.println("El filtro está siendo llamado.");
    }
}
