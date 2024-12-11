package com.alura.challenge.forohub.infra.security;

import com.alura.challenge.forohub.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {


    @Value("${forohub.security.secret}")
    private String forohubSecret;

    public String generateToken(User user) {
        try {
//            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            Algorithm algorithm = Algorithm.HMAC256(forohubSecret);
            return JWT.create()
                    .withIssuer("forohub")
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getId())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }
    }

    private Instant generateExpirationDate() {

        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
