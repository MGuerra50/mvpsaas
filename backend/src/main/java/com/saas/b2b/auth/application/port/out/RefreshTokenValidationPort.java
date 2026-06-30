package com.saas.b2b.auth.application.port.out;

import java.util.Optional;

public interface RefreshTokenValidationPort {

	Optional<ValidatedRefreshToken> validate(String refreshToken);

	record ValidatedRefreshToken(Long userId, Long tenantId, String email) {
	}
}
