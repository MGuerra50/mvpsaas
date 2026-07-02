package com.saas.b2b.budget.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saas.b2b.support.ApiTestSupport;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DashboardControllerIntegrationTest {

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
	void deveRetornarMetricasDoDashboard() throws Exception {
		mockMvc.perform(get("/api/dashboard/metrics")
						.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.grossRevenue").value(1500.00))
				.andExpect(jsonPath("$.monthlyRevenue").isArray())
				.andExpect(jsonPath("$.monthlyRevenue[0].month").value("2026-06"))
				.andExpect(jsonPath("$.monthlyRevenue[0].amount").value(1500.00))
				.andExpect(jsonPath("$.statusSummary.draft.count").value(1))
				.andExpect(jsonPath("$.statusSummary.draft.totalAmount").value(800.00))
				.andExpect(jsonPath("$.statusSummary.approved.count").value(1))
				.andExpect(jsonPath("$.statusSummary.approved.totalAmount").value(1500.00))
				.andExpect(jsonPath("$.statusSummary.rejected.count").value(0))
				.andExpect(jsonPath("$.statusSummary.rejected.totalAmount").value(0))
				.andExpect(jsonPath("$.averageTicket").value(1500.00))
				.andExpect(jsonPath("$.conversionRate").value(100.0))
				.andExpect(jsonPath("$.topCustomers").isArray())
				.andExpect(jsonPath("$.topCustomers[0].customerId").value(1))
				.andExpect(jsonPath("$.topCustomers[0].customerName").value("Cliente Alpha 1"))
				.andExpect(jsonPath("$.topCustomers[0].totalAmount").value(1500.00))
				.andExpect(jsonPath("$.topCustomers[0].budgetCount").value(1))
				.andExpect(jsonPath("$.recentBudgets").isArray())
				.andExpect(jsonPath("$.recentBudgets.length()").value(2))
				.andExpect(jsonPath("$.recentBudgets[0].id").value(2))
				.andExpect(jsonPath("$.recentBudgets[0].customerName").value("Cliente Alpha 2"))
				.andExpect(jsonPath("$.recentBudgets[0].status").value("DRAFT"))
				.andExpect(jsonPath("$.recentBudgets[1].id").value(1))
				.andExpect(jsonPath("$.recentBudgets[1].status").value("APPROVED"));
	}
}
