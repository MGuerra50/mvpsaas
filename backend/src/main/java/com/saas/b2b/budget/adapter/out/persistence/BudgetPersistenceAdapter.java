package com.saas.b2b.budget.adapter.out.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.saas.b2b.budget.application.port.out.BudgetRepositoryPort;
import com.saas.b2b.budget.domain.model.Budget;
import com.saas.b2b.budget.domain.model.BudgetStatus;
import com.saas.b2b.budget.domain.model.MonthlyRevenue;

@Component
public class BudgetPersistenceAdapter implements BudgetRepositoryPort {

	private final BudgetJpaRepository jpaRepository;

	public BudgetPersistenceAdapter(BudgetJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<Budget> findById(Long id) {
		return jpaRepository.findById(id).map(BudgetPersistenceMapper::toDomain);
	}

	@Override
	public List<Budget> findAllByTenantId(Long tenantId) {
		return jpaRepository.findByTenantId(tenantId).stream()
				.map(BudgetPersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public List<Budget> findAllByTenantIdAndStatus(Long tenantId, BudgetStatus status) {
		return jpaRepository.findByTenantIdAndStatus(tenantId, status).stream()
				.map(BudgetPersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public Budget save(Budget budget) {
		BudgetJpaEntity saved = jpaRepository.save(BudgetPersistenceMapper.toEntity(budget));
		return BudgetPersistenceMapper.toDomain(saved);
	}

	@Override
	public BigDecimal sumApprovedTotalByTenantId(Long tenantId) {
		return jpaRepository.sumApprovedTotalByTenantId(tenantId);
	}

	@Override
	public List<MonthlyRevenue> sumApprovedByMonth(Long tenantId) {
		return jpaRepository.sumApprovedByMonth(tenantId).stream()
				.map(row -> new MonthlyRevenue(row.getRevenueMonth(), row.getAmount()))
				.toList();
	}
}
