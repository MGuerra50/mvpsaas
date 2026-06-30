package com.saas.b2b.budget.domain.model;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Budget {

	private final Long id;
	private final Long tenantId;
	private final Long customerId;
	private final BigDecimal totalAmount;
	private final BudgetStatus status;
}
