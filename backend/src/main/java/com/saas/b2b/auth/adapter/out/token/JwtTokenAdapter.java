package com.saas.b2b.auth.adapter.out.token;

import org.springframework.stereotype.Component;

import com.saas.b2b.auth.application.port.out.RefreshTokenValidationPort;
import com.saas.b2b.auth.application.port.out.TokenGenerationPort;
import com.saas.b2b.auth.domain.model.User;
import com.saas.b2b.shared.security.JwtProperties;
import com.saas.b2b.shared.security.JwtTokenProvider;

import io.jsonwebtoken.Claims;

@Component
public class JwtTokenAdapter implements TokenGenerationPort, RefreshTokenValidationPort {

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtProperties jwtProperties;

	public JwtTokenAdapter(JwtTokenProvider jwtTokenProvider, JwtProperties jwtProperties) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.jwtProperties = jwtProperties;
	}

	@Override
	public String generateAccessToken(User user) {
		return jwtTokenProvider.generateAccessToken(user.getId(), user.getTenantId(), user.getEmail());
	}

	@Override
	public String generateRefreshToken(User user) {
		return jwtTokenProvider.generateRefreshToken(user.getId(), user.getTenantId(), user.getEmail());
	}

	@Override
	public java.util.Optional<ValidatedRefreshToken> validate(String refreshToken) {
		return jwtTokenProvider.parseToken(refreshToken)
				.filter(jwtTokenProvider::isRefreshToken)
				.flatMap(this::toValidatedRefreshToken);
	}

	private java.util.Optional<ValidatedRefreshToken> toValidatedRefreshToken(Claims claims) {
		return jwtTokenProvider.extractUserId(claims)
				.flatMap(userId -> jwtTokenProvider.extractTenantId(claims)
						.map(tenantId -> new ValidatedRefreshToken(
								userId,
								tenantId,
								(String) claims.get(jwtProperties.getEmailClaim()))));
	}
}
