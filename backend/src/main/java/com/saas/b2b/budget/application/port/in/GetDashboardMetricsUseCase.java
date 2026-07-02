package com.saas.b2b.budget.application.port.in;

import java.math.BigDecimal;
import java.util.List;

import com.saas.b2b.budget.domain.model.DashboardStatusSummary;
import com.saas.b2b.budget.domain.model.MonthlyRevenue;
import com.saas.b2b.budget.domain.model.RecentBudgetSummary;
import com.saas.b2b.budget.domain.model.TopCustomerRevenue;

public interface GetDashboardMetricsUseCase {

	DashboardMetrics getMetrics();

	record DashboardMetrics(
			BigDecimal grossRevenue,
			List<MonthlyRevenue> monthlyRevenue,
			DashboardStatusSummary statusSummary,
			BigDecimal averageTicket,
			double conversionRate,
			List<TopCustomerRevenue> topCustomers,
			List<RecentBudgetSummary> recentBudgets) {
	}
}
