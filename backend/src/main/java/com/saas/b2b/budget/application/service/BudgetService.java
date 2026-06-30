package com.saas.b2b.budget.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.saas.b2b.budget.application.port.in.CreateBudgetUseCase;
import com.saas.b2b.budget.application.port.in.GetDashboardMetricsUseCase;
import com.saas.b2b.budget.application.port.in.ListBudgetsUseCase;
import com.saas.b2b.budget.application.port.in.UpdateBudgetStatusUseCase;
import com.saas.b2b.budget.application.port.out.BudgetRepositoryPort;
import com.saas.b2b.budget.domain.exception.BudgetNotFoundException;
import com.saas.b2b.budget.domain.exception.InvalidBudgetStatusTransitionException;
import com.saas.b2b.budget.domain.model.Budget;
import com.saas.b2b.budget.domain.model.BudgetStatus;
import com.saas.b2b.customer.application.port.out.CustomerRepositoryPort;
import com.saas.b2b.customer.domain.exception.CustomerNotFoundException;
import com.saas.b2b.shared.tenancy.TenantContext;

@Service
public class BudgetService implements
		ListBudgetsUseCase,
		CreateBudgetUseCase,
		UpdateBudgetStatusUseCase,
		GetDashboardMetricsUseCase {

	private final BudgetRepositoryPort budgetRepositoryPort;
	private final CustomerRepositoryPort customerRepositoryPort;

	public BudgetService(BudgetRepositoryPort budgetRepositoryPort, CustomerRepositoryPort customerRepositoryPort) {
		this.budgetRepositoryPort = budgetRepositoryPort;
		this.customerRepositoryPort = customerRepositoryPort;
	}

	@Override
	public List<Budget> list(BudgetStatus status) {
		Long tenantId = TenantContext.requireCurrentTenantId();
		if (status == null) {
			return budgetRepositoryPort.findAllByTenantId(tenantId);
		}
		return budgetRepositoryPort.findAllByTenantIdAndStatus(tenantId, status);
	}

	@Override
	public Budget create(CreateBudgetCommand command) {
		Long tenantId = TenantContext.requireCurrentTenantId();
		customerRepositoryPort.findById(command.customerId())
				.orElseThrow(() -> new CustomerNotFoundException(command.customerId()));

		Budget budget = Budget.builder()
				.tenantId(tenantId)
				.customerId(command.customerId())
				.totalAmount(command.totalAmount())
				.status(BudgetStatus.DRAFT)
				.createdAt(Instant.now())
				.build();
		return budgetRepositoryPort.save(budget);
	}

	@Override
	public Budget updateStatus(Long id, BudgetStatus status) {
		Budget existing = budgetRepositoryPort.findById(id)
				.orElseThrow(() -> new BudgetNotFoundException(id));

		if (existing.getStatus() != BudgetStatus.DRAFT) {
			throw new InvalidBudgetStatusTransitionException();
		}
		if (status != BudgetStatus.APPROVED && status != BudgetStatus.REJECTED) {
			throw new InvalidBudgetStatusTransitionException();
		}

		Budget updated = Budget.builder()
				.id(existing.getId())
				.tenantId(existing.getTenantId())
				.customerId(existing.getCustomerId())
				.totalAmount(existing.getTotalAmount())
				.status(status)
				.createdAt(existing.getCreatedAt())
				.build();
		return budgetRepositoryPort.save(updated);
	}

	@Override
	public DashboardMetrics getMetrics() {
		Long tenantId = TenantContext.requireCurrentTenantId();
		return new DashboardMetrics(
				budgetRepositoryPort.sumApprovedTotalByTenantId(tenantId),
				budgetRepositoryPort.sumApprovedByMonth(tenantId));
	}
}
