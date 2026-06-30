package com.saas.b2b.budget.domain.exception;

import com.saas.b2b.shared.exception.ResourceNotFoundException;

public class BudgetNotFoundException extends ResourceNotFoundException {

	public BudgetNotFoundException(Long id) {
		super("Orçamento não encontrado: " + id);
	}
}
