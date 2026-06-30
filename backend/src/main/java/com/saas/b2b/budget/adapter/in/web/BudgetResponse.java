package com.saas.b2b.budget.adapter.in.web;

import java.math.BigDecimal;
import java.time.Instant;

import com.saas.b2b.budget.domain.model.Budget;
import com.saas.b2b.budget.domain.model.BudgetStatus;

public record BudgetResponse(
		Long id,
		Long tenantId,
		Long customerId,
		BigDecimal totalAmount,
		BudgetStatus status,
		Instant createdAt) {

	public static BudgetResponse from(Budget budget) {
		return new BudgetResponse(
				budget.getId(),
				budget.getTenantId(),
				budget.getCustomerId(),
				budget.getTotalAmount(),
				budget.getStatus(),
				budget.getCreatedAt());
	}
}
