package com.saas.b2b.budget.application.port.in;

import java.util.List;

import com.saas.b2b.budget.domain.model.Budget;
import com.saas.b2b.budget.domain.model.BudgetStatus;

public interface ListBudgetsUseCase {

	List<Budget> list(BudgetStatus status);
}
