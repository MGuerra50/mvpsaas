package com.saas.b2b.budget.adapter.out.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.saas.b2b.budget.application.port.out.BudgetRepositoryPort;
import com.saas.b2b.budget.domain.model.Budget;
import com.saas.b2b.budget.domain.model.BudgetStatus;
import com.saas.b2b.budget.domain.model.BudgetStatusMetric;
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

	@Override
	public BudgetStatusMetric aggregateByTenantIdAndStatus(Long tenantId, BudgetStatus status) {
		BudgetJpaRepository.StatusAggregateProjection result =
				jpaRepository.aggregateByTenantIdAndStatus(tenantId, status.name());
		long count = result.getBudgetCount() != null ? result.getBudgetCount() : 0L;
		BigDecimal total = result.getRevenueTotal() != null ? result.getRevenueTotal() : BigDecimal.ZERO;
		return new BudgetStatusMetric(count, total);
	}

	@Override
	public List<TopCustomerAggregate> findTopCustomersByApprovedRevenue(Long tenantId, int limit) {
		return jpaRepository.findTopCustomersByApprovedRevenue(tenantId).stream()
				.limit(limit)
				.map(row -> new TopCustomerAggregate(
						row.getCustomerId(),
						row.getRevenueTotal(),
						row.getBudgetCount()))
				.toList();
	}

	@Override
	public List<Budget> findRecentByTenantId(Long tenantId, int limit) {
		return jpaRepository.findTop5ByTenantIdOrderByCreatedAtDesc(tenantId).stream()
				.limit(limit)
				.map(BudgetPersistenceMapper::toDomain)
				.toList();
	}
}
