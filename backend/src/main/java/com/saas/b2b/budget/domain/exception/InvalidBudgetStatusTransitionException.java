package com.saas.b2b.budget.domain.exception;

public class InvalidBudgetStatusTransitionException extends IllegalArgumentException {

	public InvalidBudgetStatusTransitionException() {
		super("Transição de status inválida para o orçamento");
	}
}
