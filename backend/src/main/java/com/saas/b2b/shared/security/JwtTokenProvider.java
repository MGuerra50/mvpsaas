package com.saas.b2b.shared.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final JwtProperties jwtProperties;
	private final SecretKey secretKey;

	public JwtTokenProvider(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
	}

	public String generateAccessToken(Long userId, Long tenantId, String email) {
		return buildToken(userId, tenantId, email, jwtProperties.getAccessTokenType(),
				jwtProperties.getAccessTokenExpiration());
	}

	public String generateRefreshToken(Long userId, Long tenantId, String email) {
		return buildToken(userId, tenantId, email, jwtProperties.getRefreshTokenType(),
				jwtProperties.getRefreshTokenExpiration());
	}

	public Optional<Claims> parseToken(String token) {
		if (token == null || token.isBlank()) {
			return Optional.empty();
		}
		try {
			Claims claims = Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(token)
					.getPayload();
			return Optional.of(claims);
		}
		catch (RuntimeException ex) {
			return Optional.empty();
		}
	}

	public Optional<Long> extractTenantId(Claims claims) {
		return extractLongClaim(claims, jwtProperties.getTenantIdClaim());
	}

	public Optional<Long> extractTenantId(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return Optional.empty();
		}
		String token = authorizationHeader.substring(7).trim();
		return parseToken(token).flatMap(this::extractTenantId);
	}

	public Optional<Long> extractUserId(Claims claims) {
		return extractLongClaim(claims, jwtProperties.getUserIdClaim());
	}

	public boolean isAccessToken(Claims claims) {
		return jwtProperties.getAccessTokenType().equals(claims.get(jwtProperties.getTokenTypeClaim()));
	}

	public boolean isRefreshToken(Claims claims) {
		return jwtProperties.getRefreshTokenType().equals(claims.get(jwtProperties.getTokenTypeClaim()));
	}

	private String buildToken(Long userId, Long tenantId, String email, String tokenType,
			java.time.Duration expiration) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + expiration.toMillis());

		return Jwts.builder()
				.claim(jwtProperties.getUserIdClaim(), userId)
				.claim(jwtProperties.getTenantIdClaim(), tenantId)
				.claim(jwtProperties.getEmailClaim(), email)
				.claim(jwtProperties.getTokenTypeClaim(), tokenType)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(secretKey)
				.compact();
	}

	private Optional<Long> extractLongClaim(Claims claims, String claimName) {
		Object value = claims.get(claimName);
		if (value instanceof Number number) {
			return Optional.of(number.longValue());
		}
		if (value instanceof String stringValue) {
			return Optional.of(Long.parseLong(stringValue));
		}
		return Optional.empty();
	}
}
