package com.example.school.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.school.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JWTService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateJWT(User user){
        var algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("School API")
                .withSubject(user.getUsername())
                .withClaim("user-type", user.getUserType().toString())
                .withExpiresAt(expiresDate())
                .sign(algorithm);
    }

    public String getSubject(String jWTToken){
        var algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("School API")
                .build()
                .verify(jWTToken)
                .getSubject();

    }
    private Instant expiresDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
