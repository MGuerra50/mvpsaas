package com.saas.b2b.customer.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
class CustomerControllerIntegrationTest {

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
	void deveListarClientesComPaginacao() throws Exception {
		mockMvc.perform(get("/api/customers")
						.header("Authorization", "Bearer " + accessToken)
						.param("page", "0")
						.param("size", "10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.totalElements").value(2));
	}

	@Test
	void deveCriarEAtualizarCliente() throws Exception {
		String createBody = """
				{"name":"Novo Cliente","phone":"11988887777","documentId":"99999999999"}
				""";
		mockMvc.perform(post("/api/customers")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(createBody))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Novo Cliente"));

		mockMvc.perform(get("/api/customers/1")
						.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1));

		String updateBody = """
				{"name":"Cliente Atualizado","phone":"11977776666","documentId":"11111111101"}
				""";
		mockMvc.perform(put("/api/customers/1")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Cliente Atualizado"));
	}

	@Test
	void deveExcluirCliente() throws Exception {
		mockMvc.perform(delete("/api/customers/2")
						.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isNoContent());
	}
}
