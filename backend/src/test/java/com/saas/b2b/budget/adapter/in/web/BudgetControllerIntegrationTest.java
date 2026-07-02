package com.saas.b2b.budget.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
class BudgetControllerIntegrationTest {

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
	void deveListarOrcamentosPorStatus() throws Exception {
		mockMvc.perform(get("/api/budgets")
						.header("Authorization", "Bearer " + accessToken)
						.param("status", "DRAFT"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].status").value("DRAFT"));
	}

	@Test
	void deveCriarOrcamentoComoDraft() throws Exception {
		String body = """
				{"customerId":1,"totalAmount":999.99}
				""";
		mockMvc.perform(post("/api/budgets")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.status").value("DRAFT"))
				.andExpect(jsonPath("$.id").value(4));
	}

	@Test
	void deveAtualizarStatusDoOrcamento() throws Exception {
		String body = """
				{"status":"APPROVED"}
				""";
		mockMvc.perform(patch("/api/budgets/2/status")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("APPROVED"));
	}
}
