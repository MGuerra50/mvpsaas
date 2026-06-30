package com.saas.b2b.shared.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.saas.b2b.support.TestJwtFactory;

@SpringBootTest
@ActiveProfiles("test")
class JwtTenantExtractorTest {

	private static final String SECRET = "orcazap-dev-secret-key-minimum-32-chars!!";

	@Autowired
	private JwtTenantExtractor jwtTenantExtractor;

	@Test
	void deveExtrairTenantIdDeTokenValido() {
		String token = TestJwtFactory.createToken(1L, SECRET);

		var tenantId = jwtTenantExtractor.extractTenantId("Bearer " + token);

		assertThat(tenantId).contains(1L);
	}

	@Test
	void deveRetornarVazioQuandoHeaderAusente() {
		assertThat(jwtTenantExtractor.extractTenantId(null)).isEmpty();
	}
}
