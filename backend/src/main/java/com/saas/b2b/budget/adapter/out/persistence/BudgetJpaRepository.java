package com.saas.b2b.budget.adapter.out.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetJpaRepository extends JpaRepository<BudgetJpaEntity, Long> {

	List<BudgetJpaEntity> findByTenantId(Long tenantId);
}
