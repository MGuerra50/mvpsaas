package com.saas.b2b.budget.application.port.in;

import java.math.BigDecimal;

import com.saas.b2b.budget.domain.model.Budget;

public interface CreateBudgetUseCase {

	Budget create(CreateBudgetCommand command);

	record CreateBudgetCommand(Long customerId, BigDecimal totalAmount) {
	}
}
