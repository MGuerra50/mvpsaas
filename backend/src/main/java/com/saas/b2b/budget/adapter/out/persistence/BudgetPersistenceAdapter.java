package com.saas.b2b.budget.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.saas.b2b.budget.application.port.out.BudgetRepositoryPort;
import com.saas.b2b.budget.domain.model.Budget;

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
	public Budget save(Budget budget) {
		BudgetJpaEntity saved = jpaRepository.save(BudgetPersistenceMapper.toEntity(budget));
		return BudgetPersistenceMapper.toDomain(saved);
	}
}
