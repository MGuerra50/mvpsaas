package com.saas.b2b.budget.adapter.in.web;

import java.math.BigDecimal;
import java.util.List;

import com.saas.b2b.budget.application.port.in.GetDashboardMetricsUseCase;
import com.saas.b2b.budget.domain.model.MonthlyRevenue;

public record DashboardMetricsResponse(
		BigDecimal grossRevenue,
		List<MonthlyRevenueResponse> monthlyRevenue) {

	public static DashboardMetricsResponse from(GetDashboardMetricsUseCase.DashboardMetrics metrics) {
		return new DashboardMetricsResponse(
				metrics.grossRevenue(),
				metrics.monthlyRevenue().stream().map(MonthlyRevenueResponse::from).toList());
	}

	public record MonthlyRevenueResponse(String month, BigDecimal amount) {
		public static MonthlyRevenueResponse from(MonthlyRevenue revenue) {
			return new MonthlyRevenueResponse(revenue.month(), revenue.amount());
		}
	}
}
