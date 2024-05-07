package io.uex.listadecontatos.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import io.uex.listadecontatos.entity.User;

@Service
public class TokenService {

	public String generateToken(User user) {
        try {
        	var algorithm = Algorithm.HMAC256("12345678");
        	return JWT.create()
        			.withIssuer("API lista de contatos")
        			.withSubject(user.getEmail())
        			.withExpiresAt(dateExpire())
        			.sign(algorithm);
        } catch (JWTCreationException exception){
        	throw new RuntimeException("erro ao gerar token jwt", exception);
        }

	}
	
	public String getSubject(String tokenJWT) {
		try {
			var algorithm = Algorithm.HMAC256("12345678");
			return JWT.require(algorithm)
					.withIssuer("API lista de contatos")
					.build()
					.verify(tokenJWT)
					.getSubject();
		}catch (JWTVerificationException e) {
			throw new RuntimeException("Token inválido.");
		}
	}

	private Instant dateExpire() {
		return LocalDateTime.now().plusHours(23).plusMinutes(59).toInstant(ZoneOffset.of("-03:00"));
	}
}
