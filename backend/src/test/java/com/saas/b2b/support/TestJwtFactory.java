package com.saas.b2b.support;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public final class TestJwtFactory {

	private TestJwtFactory() {
	}

	public static String createToken(Long tenantId, String secret) {
		SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		return Jwts.builder()
				.claim("tenant_id", tenantId)
				.issuedAt(new Date())
				.signWith(key)
				.compact();
	}
}
