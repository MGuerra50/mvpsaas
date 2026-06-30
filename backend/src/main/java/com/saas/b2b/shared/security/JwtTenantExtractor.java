package com.saas.b2b.shared.security;

import org.springframework.stereotype.Component;

@Component
public class JwtTenantExtractor {

	private final JwtTokenProvider jwtTokenProvider;

	public JwtTenantExtractor(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public java.util.Optional<Long> extractTenantId(String authorizationHeader) {
		return jwtTokenProvider.extractTenantId(authorizationHeader);
	}
}
