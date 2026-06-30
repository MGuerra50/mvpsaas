package com.saas.b2b.auth.domain.exception;

public class InvalidRefreshTokenException extends RuntimeException {

	public InvalidRefreshTokenException() {
		super("Refresh token inválido ou expirado");
	}
}
