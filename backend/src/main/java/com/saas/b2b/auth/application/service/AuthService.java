package com.saas.b2b.auth.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saas.b2b.auth.application.port.in.LoginCommand;
import com.saas.b2b.auth.application.port.in.LoginResult;
import com.saas.b2b.auth.application.port.in.LoginUseCase;
import com.saas.b2b.auth.application.port.in.RefreshTokenUseCase;
import com.saas.b2b.auth.application.port.in.RefreshTokenResult;
import com.saas.b2b.auth.application.port.out.RefreshTokenValidationPort;
import com.saas.b2b.auth.application.port.out.TokenGenerationPort;
import com.saas.b2b.auth.application.port.out.UserRepositoryPort;
import com.saas.b2b.auth.domain.exception.InvalidCredentialsException;
import com.saas.b2b.auth.domain.exception.InvalidRefreshTokenException;
import com.saas.b2b.auth.domain.model.User;

@Service
public class AuthService implements LoginUseCase, RefreshTokenUseCase {

	private final UserRepositoryPort userRepositoryPort;
	private final TokenGenerationPort tokenGenerationPort;
	private final RefreshTokenValidationPort refreshTokenValidationPort;
	private final PasswordEncoder passwordEncoder;

	public AuthService(
			UserRepositoryPort userRepositoryPort,
			TokenGenerationPort tokenGenerationPort,
			RefreshTokenValidationPort refreshTokenValidationPort,
			PasswordEncoder passwordEncoder) {
		this.userRepositoryPort = userRepositoryPort;
		this.tokenGenerationPort = tokenGenerationPort;
		this.refreshTokenValidationPort = refreshTokenValidationPort;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public LoginResult login(LoginCommand command) {
		User user = userRepositoryPort.findByEmail(command.email())
				.orElseThrow(InvalidCredentialsException::new);

		if (!passwordEncoder.matches(command.password(), user.getPasswordHash())) {
			throw new InvalidCredentialsException();
		}

		return new LoginResult(
				tokenGenerationPort.generateAccessToken(user),
				tokenGenerationPort.generateRefreshToken(user));
	}

	@Override
	public RefreshTokenResult refresh(String refreshToken) {
		RefreshTokenValidationPort.ValidatedRefreshToken validated = refreshTokenValidationPort
				.validate(refreshToken)
				.orElseThrow(InvalidRefreshTokenException::new);

		User user = userRepositoryPort.findById(validated.userId())
				.orElseThrow(InvalidRefreshTokenException::new);

		if (!user.getTenantId().equals(validated.tenantId())) {
			throw new InvalidRefreshTokenException();
		}

		return new RefreshTokenResult(tokenGenerationPort.generateAccessToken(user));
	}
}
