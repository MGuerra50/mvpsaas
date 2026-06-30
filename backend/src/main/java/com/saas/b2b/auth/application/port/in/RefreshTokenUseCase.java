package com.saas.b2b.auth.application.port.in;

public interface RefreshTokenUseCase {

	RefreshTokenResult refresh(String refreshToken);
}
