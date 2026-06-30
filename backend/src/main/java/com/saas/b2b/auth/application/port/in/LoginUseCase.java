package com.saas.b2b.auth.application.port.in;

public interface LoginUseCase {

	LoginResult login(LoginCommand command);
}
