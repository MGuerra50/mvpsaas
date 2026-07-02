package com.saas.b2b.budget.domain.model;

import java.math.BigDecimal;

public record BudgetStatusMetric(long count, BigDecimal totalAmount) {

	public static BudgetStatusMetric empty() {
		return new BudgetStatusMetric(0, BigDecimal.ZERO);
	}
}
