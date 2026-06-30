package com.saas.b2b.budget.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saas.b2b.budget.application.port.in.GetDashboardMetricsUseCase;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	private final GetDashboardMetricsUseCase getDashboardMetricsUseCase;

	public DashboardController(GetDashboardMetricsUseCase getDashboardMetricsUseCase) {
		this.getDashboardMetricsUseCase = getDashboardMetricsUseCase;
	}

	@GetMapping("/metrics")
	DashboardMetricsResponse getMetrics() {
		return DashboardMetricsResponse.from(getDashboardMetricsUseCase.getMetrics());
	}
}
