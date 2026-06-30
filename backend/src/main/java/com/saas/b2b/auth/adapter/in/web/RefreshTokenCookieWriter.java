package com.saas.b2b.auth.adapter.in.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.saas.b2b.shared.security.JwtProperties;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class RefreshTokenCookieWriter {

	private final JwtProperties jwtProperties;

	public RefreshTokenCookieWriter(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	public void write(HttpServletResponse response, String refreshToken) {
		ResponseCookie cookie = ResponseCookie.from(jwtProperties.getRefreshTokenCookieName(), refreshToken)
				.httpOnly(true)
				.secure(false)
				.path("/")
				.maxAge(jwtProperties.getRefreshTokenExpiration())
				.sameSite("Lax")
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}

	public void clear(HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from(jwtProperties.getRefreshTokenCookieName(), "")
				.httpOnly(true)
				.secure(false)
				.path("/")
				.maxAge(0)
				.sameSite("Lax")
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
}
