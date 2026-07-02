package com.saas.b2b.auth.domain.exception;

public class InvalidCurrentPasswordException extends RuntimeException {

	public InvalidCurrentPasswordException() {
		super("Senha atual incorreta.");
	}
}
