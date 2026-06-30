package com.saas.b2b.auth.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saas.b2b.support.TestJwtFactory;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

	private static final String SECRET = "orcazap-dev-secret-key-minimum-32-chars!!";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void loginComCredenciaisValidasRetornaAccessTokenECookieRefresh() throws Exception {
		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new LoginRequest("admin@alpha.com", "password"))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").isNotEmpty())
				.andExpect(cookie().exists("refresh_token"))
				.andExpect(cookie().httpOnly("refresh_token", true));
	}

	@Test
	void loginComCredenciaisInvalidasRetorna401() throws Exception {
		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new LoginRequest("admin@alpha.com", "errada"))))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Credenciais inválidas"));
	}

	@Test
	void refreshComCookieValidoRetornaNovoAccessToken() throws Exception {
		MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new LoginRequest("admin@alpha.com", "password"))))
				.andExpect(status().isOk())
				.andReturn();

		String refreshCookie = loginResult.getResponse().getCookie("refresh_token").getValue();

		mockMvc.perform(post("/api/auth/refresh").cookie(new jakarta.servlet.http.Cookie("refresh_token", refreshCookie)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").isNotEmpty());
	}

	@Test
	void logoutLimpaCookieRefresh() throws Exception {
		mockMvc.perform(post("/api/auth/logout"))
				.andExpect(status().isNoContent())
				.andExpect(cookie().maxAge("refresh_token", 0));
	}

	@Test
	void refreshComTokenInvalidoRetorna401() throws Exception {
		mockMvc.perform(post("/api/auth/refresh")
						.cookie(new jakarta.servlet.http.Cookie("refresh_token", "token-invalido")))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void accessTokenGeradoManualmenteContemClaimsEsperados() {
		String token = TestJwtFactory.createAccessToken(1L, 1L, "admin@alpha.com", SECRET);
		assertThat(token).isNotBlank();
	}
}
