package com.saas.b2b.shared.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

	private String secret = "orcazap-dev-secret-key-minimum-32-chars!!";
	private String tenantIdClaim = "tenant_id";

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
}
