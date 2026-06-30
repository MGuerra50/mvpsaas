package com.saas.b2b.budget.adapter.in.web;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CreateBudgetRequest(
		@NotNull Long customerId,
		@NotNull @DecimalMin("0.01") BigDecimal totalAmount) {
}
