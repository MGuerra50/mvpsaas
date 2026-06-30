package com.saas.b2b.budget.application.port.out;

import java.util.List;
import java.util.Optional;

import com.saas.b2b.budget.domain.model.Budget;

public interface BudgetRepositoryPort {

	Optional<Budget> findById(Long id);

	List<Budget> findAllByTenantId(Long tenantId);

	Budget save(Budget budget);
}
