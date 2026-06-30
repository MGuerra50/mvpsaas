package com.saas.b2b.support;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ApiTestSupport {

	private ApiTestSupport() {
	}

	public static String obtainAccessToken(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
		String body = """
				{"email":"admin@alpha.com","password":"password"}
				""";
		MvcResult result = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andReturn();
		JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
		return json.get("accessToken").asText();
	}
}
