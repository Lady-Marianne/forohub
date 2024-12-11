package com.alura.challenge.forohub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public String generateToken() {
        try {
//            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            Algorithm algorithm = Algorithm.HMAC256("clavesecreta123");
            return JWT.create()
                    .withIssuer("forohub")
                    .withSubject("mariana.lois")
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }
    }
}
