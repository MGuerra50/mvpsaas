package com.saas.b2b.auth.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saas.b2b.auth.application.port.in.LoginCommand;
import com.saas.b2b.auth.application.port.in.LoginUseCase;
import com.saas.b2b.auth.application.port.in.RefreshTokenUseCase;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final LoginUseCase loginUseCase;
	private final RefreshTokenUseCase refreshTokenUseCase;
	private final RefreshTokenCookieWriter refreshTokenCookieWriter;

	public AuthController(
			LoginUseCase loginUseCase,
			RefreshTokenUseCase refreshTokenUseCase,
			RefreshTokenCookieWriter refreshTokenCookieWriter) {
		this.loginUseCase = loginUseCase;
		this.refreshTokenUseCase = refreshTokenUseCase;
		this.refreshTokenCookieWriter = refreshTokenCookieWriter;
	}

	@PostMapping("/login")
	ResponseEntity<AuthResponse> login(
			@Valid @RequestBody LoginRequest request,
			HttpServletResponse response) {
		var result = loginUseCase.login(new LoginCommand(request.email(), request.password()));
		refreshTokenCookieWriter.write(response, result.refreshToken());
		return ResponseEntity.ok(new AuthResponse(result.accessToken()));
	}

	@PostMapping("/refresh")
	ResponseEntity<AuthResponse> refresh(
			@CookieValue(name = "${app.jwt.refresh-token-cookie-name:refresh_token}", required = false)
			String refreshToken) {
		var result = refreshTokenUseCase.refresh(refreshToken);
		return ResponseEntity.ok(new AuthResponse(result.accessToken()));
	}

	@PostMapping("/logout")
	ResponseEntity<Void> logout(HttpServletResponse response) {
		refreshTokenCookieWriter.clear(response);
		return ResponseEntity.noContent().build();
	}
}
