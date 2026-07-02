package com.saas.b2b.budget.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record RecentBudgetSummary(
		Long id,
		Long customerId,
		String customerName,
		BigDecimal totalAmount,
		BudgetStatus status,
		Instant createdAt) {
}
