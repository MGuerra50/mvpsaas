package com.saas.b2b.budget.adapter.out.persistence;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.saas.b2b.budget.domain.model.BudgetStatus;

@Repository
public interface BudgetJpaRepository extends JpaRepository<BudgetJpaEntity, Long> {

	List<BudgetJpaEntity> findByTenantId(Long tenantId);

	List<BudgetJpaEntity> findByTenantIdAndStatus(Long tenantId, BudgetStatus status);

	@Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM BudgetJpaEntity b WHERE b.tenantId = :tenantId AND b.status = 'APPROVED'")
	BigDecimal sumApprovedTotalByTenantId(@Param("tenantId") Long tenantId);

	@Query(value = """
			SELECT FORMATDATETIME(created_at, 'yyyy-MM') AS revenue_month,
			       SUM(total_amount) AS amount
			FROM budgets
			WHERE tenant_id = :tenantId AND status = 'APPROVED'
			GROUP BY FORMATDATETIME(created_at, 'yyyy-MM')
			ORDER BY revenue_month
			""", nativeQuery = true)
	List<MonthlyRevenueProjection> sumApprovedByMonth(@Param("tenantId") Long tenantId);

	interface MonthlyRevenueProjection {
		String getRevenueMonth();

		BigDecimal getAmount();
	}
}
