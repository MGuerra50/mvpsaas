package com.saas.b2b.budget.application.port.in;

import java.math.BigDecimal;
import java.util.List;

import com.saas.b2b.budget.domain.model.MonthlyRevenue;

public interface GetDashboardMetricsUseCase {

	DashboardMetrics getMetrics();

	record DashboardMetrics(BigDecimal grossRevenue, List<MonthlyRevenue> monthlyRevenue) {
	}
}
