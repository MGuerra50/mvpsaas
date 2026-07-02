package com.saas.b2b.budget.domain.model;

import java.math.BigDecimal;

public record TopCustomerRevenue(
		Long customerId,
		String customerName,
		BigDecimal totalAmount,
		long budgetCount) {
}
