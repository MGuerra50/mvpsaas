package com.saas.b2b.shared.tenancy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class TenantContextTest {

	@AfterEach
	void tearDown() {
		TenantContext.clear();
	}

	@Test
	void deveArmazenarERecuperarTenantId() {
		TenantContext.setCurrentTenantId(42L);
		assertThat(TenantContext.getCurrentTenantId()).isEqualTo(42L);
	}

	@Test
	void deveLimparTenantId() {
		TenantContext.setCurrentTenantId(42L);
		TenantContext.clear();
		assertThat(TenantContext.getCurrentTenantId()).isNull();
	}
}
