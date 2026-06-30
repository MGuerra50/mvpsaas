package com.saas.b2b.budget.adapter.out.persistence;

import com.saas.b2b.budget.domain.model.Budget;

public final class BudgetPersistenceMapper {

	private BudgetPersistenceMapper() {
	}

	public static Budget toDomain(BudgetJpaEntity entity) {
		return Budget.builder()
				.id(entity.getId())
				.tenantId(entity.getTenantId())
				.customerId(entity.getCustomerId())
				.totalAmount(entity.getTotalAmount())
				.status(entity.getStatus())
				.build();
	}

	public static BudgetJpaEntity toEntity(Budget budget) {
		BudgetJpaEntity entity = new BudgetJpaEntity();
		entity.setId(budget.getId());
		entity.setTenantId(budget.getTenantId());
		entity.setCustomerId(budget.getCustomerId());
		entity.setTotalAmount(budget.getTotalAmount());
		entity.setStatus(budget.getStatus());
		return entity;
	}
}
