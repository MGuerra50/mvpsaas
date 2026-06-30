package com.saas.b2b.auth.application.port.out;

import com.saas.b2b.auth.domain.model.User;

public interface TokenGenerationPort {

	String generateAccessToken(User user);

	String generateRefreshToken(User user);
}
