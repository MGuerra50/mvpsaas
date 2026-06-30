package com.saas.b2b.auth.domain.exception;

public class InvalidCredentialsException extends RuntimeException {

	public InvalidCredentialsException() {
		super("Credenciais inválidas");
	}
}
