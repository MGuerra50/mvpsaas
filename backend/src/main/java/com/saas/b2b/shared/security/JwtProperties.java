package com.saas.b2b.shared.security;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

	private String secret = "orcazap-dev-secret-key-minimum-32-chars!!";
	private String tenantIdClaim = "tenant_id";
	private String userIdClaim = "user_id";
	private String emailClaim = "email";
	private String tokenTypeClaim = "token_type";
	private String accessTokenType = "access";
	private String refreshTokenType = "refresh";
	private Duration accessTokenExpiration = Duration.ofMinutes(15);
	private Duration refreshTokenExpiration = Duration.ofDays(7);
	private String refreshTokenCookieName = "refresh_token";

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getTenantIdClaim() {
		return tenantIdClaim;
	}

	public void setTenantIdClaim(String tenantIdClaim) {
		this.tenantIdClaim = tenantIdClaim;
	}

	public String getUserIdClaim() {
		return userIdClaim;
	}

	public void setUserIdClaim(String userIdClaim) {
		this.userIdClaim = userIdClaim;
	}

	public String getEmailClaim() {
		return emailClaim;
	}

	public void setEmailClaim(String emailClaim) {
		this.emailClaim = emailClaim;
	}

	public String getTokenTypeClaim() {
		return tokenTypeClaim;
	}

	public void setTokenTypeClaim(String tokenTypeClaim) {
		this.tokenTypeClaim = tokenTypeClaim;
	}

	public String getAccessTokenType() {
		return accessTokenType;
	}

	public void setAccessTokenType(String accessTokenType) {
		this.accessTokenType = accessTokenType;
	}

	public String getRefreshTokenType() {
		return refreshTokenType;
	}

	public void setRefreshTokenType(String refreshTokenType) {
		this.refreshTokenType = refreshTokenType;
	}

	public Duration getAccessTokenExpiration() {
		return accessTokenExpiration;
	}

	public void setAccessTokenExpiration(Duration accessTokenExpiration) {
		this.accessTokenExpiration = accessTokenExpiration;
	}

	public Duration getRefreshTokenExpiration() {
		return refreshTokenExpiration;
	}

	public void setRefreshTokenExpiration(Duration refreshTokenExpiration) {
		this.refreshTokenExpiration = refreshTokenExpiration;
	}

	public String getRefreshTokenCookieName() {
		return refreshTokenCookieName;
	}

	public void setRefreshTokenCookieName(String refreshTokenCookieName) {
		this.refreshTokenCookieName = refreshTokenCookieName;
	}
}
