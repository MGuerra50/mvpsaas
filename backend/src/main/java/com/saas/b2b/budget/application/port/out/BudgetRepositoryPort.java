package com.saas.b2b.budget.application.port.out;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.saas.b2b.budget.domain.model.Budget;
import com.saas.b2b.budget.domain.model.BudgetStatus;
import com.saas.b2b.budget.domain.model.MonthlyRevenue;

public interface BudgetRepositoryPort {

	Optional<Budget> findById(Long id);

	List<Budget> findAllByTenantId(Long tenantId);

	List<Budget> findAllByTenantIdAndStatus(Long tenantId, BudgetStatus status);

	Budget save(Budget budget);

	BigDecimal sumApprovedTotalByTenantId(Long tenantId);

	List<MonthlyRevenue> sumApprovedByMonth(Long tenantId);
}
