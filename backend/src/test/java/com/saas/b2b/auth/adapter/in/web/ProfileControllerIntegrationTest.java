package com.saas.b2b.auth.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saas.b2b.support.ApiTestSupport;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfileControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private String accessToken;

	@BeforeEach
	void setUp() throws Exception {
		accessToken = ApiTestSupport.obtainAccessToken(mockMvc, objectMapper);
	}

	@Test
	void deveRetornarEAtualizarPerfil() throws Exception {
		mockMvc.perform(get("/api/auth/me")
						.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("admin@alpha.com"))
				.andExpect(jsonPath("$.name").value("Admin Alpha"));

		String updateBody = """
				{"name":"Admin Alpha Atualizado","profileImageUrl":null}
				""";
		mockMvc.perform(put("/api/auth/profile")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Admin Alpha Atualizado"));
	}

	@Test
	void deveAlterarSenha() throws Exception {
		String passwordBody = """
				{"currentPassword":"password","newPassword":"password123"}
				""";
		mockMvc.perform(put("/api/auth/password")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(passwordBody))
				.andExpect(status().isNoContent());

		String revertBody = """
				{"currentPassword":"password123","newPassword":"password"}
				""";
		mockMvc.perform(put("/api/auth/password")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(revertBody))
				.andExpect(status().isNoContent());
	}
}
