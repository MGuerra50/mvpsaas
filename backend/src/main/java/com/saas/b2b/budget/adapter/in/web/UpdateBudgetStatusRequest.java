package com.saas.b2b.budget.adapter.in.web;

import com.saas.b2b.budget.domain.model.BudgetStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateBudgetStatusRequest(@NotNull BudgetStatus status) {
}
