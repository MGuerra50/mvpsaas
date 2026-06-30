package com.saas.b2b.budget.application.port.in;

import com.saas.b2b.budget.domain.model.Budget;
import com.saas.b2b.budget.domain.model.BudgetStatus;

public interface UpdateBudgetStatusUseCase {

	Budget updateStatus(Long id, BudgetStatus status);
}
