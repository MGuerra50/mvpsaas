package com.saas.b2b.budget.domain.model;

public record DashboardStatusSummary(
		BudgetStatusMetric draft,
		BudgetStatusMetric approved,
		BudgetStatusMetric rejected) {
}
