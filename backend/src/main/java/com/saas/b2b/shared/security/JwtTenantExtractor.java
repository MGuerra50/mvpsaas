package com.saas.b2b.shared.security;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTenantExtractor {

	private final JwtProperties jwtProperties;
	private final SecretKey secretKey;

	public JwtTenantExtractor(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
	}

	public Optional<Long> extractTenantId(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return Optional.empty();
		}

		String token = authorizationHeader.substring(7).trim();
		if (token.isEmpty()) {
			return Optional.empty();
		}

		try {
			Claims claims = Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(token)
					.getPayload();

			Object tenantId = claims.get(jwtProperties.getTenantIdClaim());
			if (tenantId instanceof Number number) {
				return Optional.of(number.longValue());
			}
			if (tenantId instanceof String value) {
				return Optional.of(Long.parseLong(value));
			}
			return Optional.empty();
		}
		catch (RuntimeException ex) {
			return Optional.empty();
		}
	}
}
