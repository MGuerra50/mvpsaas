package com.saas.b2b.budget.application.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.saas.b2b.budget.domain.model.BudgetStatusMetric;
import com.saas.b2b.budget.domain.model.DashboardStatusSummary;
import com.saas.b2b.budget.domain.model.RecentBudgetSummary;
import com.saas.b2b.budget.domain.model.TopCustomerRevenue;
import com.saas.b2b.customer.application.port.out.CustomerRepositoryPort;
import com.saas.b2b.customer.domain.exception.CustomerNotFoundException;
import com.saas.b2b.customer.domain.model.Customer;
import com.saas.b2b.shared.tenancy.TenantContext;

@Service
public class BudgetService implements
		ListBudgetsUseCase,
		CreateBudgetUseCase,
		UpdateBudgetStatusUseCase,
		GetDashboardMetricsUseCase {

	private static final int TOP_CUSTOMERS_LIMIT = 5;
	private static final int RECENT_BUDGETS_LIMIT = 5;

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

		BudgetStatusMetric draft = budgetRepositoryPort.aggregateByTenantIdAndStatus(tenantId, BudgetStatus.DRAFT);
		BudgetStatusMetric approved = budgetRepositoryPort.aggregateByTenantIdAndStatus(tenantId, BudgetStatus.APPROVED);
		BudgetStatusMetric rejected = budgetRepositoryPort.aggregateByTenantIdAndStatus(tenantId, BudgetStatus.REJECTED);

		BigDecimal grossRevenue = approved.totalAmount();
		List<TopCustomerRevenue> topCustomers = budgetRepositoryPort
				.findTopCustomersByApprovedRevenue(tenantId, TOP_CUSTOMERS_LIMIT)
				.stream()
				.map(aggregate -> toTopCustomerRevenue(aggregate.customerId(), aggregate.totalAmount(), aggregate.budgetCount()))
				.toList();
		List<RecentBudgetSummary> recentBudgets = budgetRepositoryPort
				.findRecentByTenantId(tenantId, RECENT_BUDGETS_LIMIT)
				.stream()
				.map(this::toRecentBudgetSummary)
				.toList();

		return new DashboardMetrics(
				grossRevenue,
				budgetRepositoryPort.sumApprovedByMonth(tenantId),
				new DashboardStatusSummary(draft, approved, rejected),
				calculateAverageTicket(grossRevenue, approved.count()),
				calculateConversionRate(approved.count(), rejected.count()),
				topCustomers,
				recentBudgets);
	}

	private TopCustomerRevenue toTopCustomerRevenue(Long customerId, BigDecimal totalAmount, long budgetCount) {
		String customerName = customerRepositoryPort.findById(customerId)
				.map(Customer::getName)
				.orElse("Cliente desconhecido");
		return new TopCustomerRevenue(customerId, customerName, totalAmount, budgetCount);
	}

	private RecentBudgetSummary toRecentBudgetSummary(Budget budget) {
		String customerName = customerRepositoryPort.findById(budget.getCustomerId())
				.map(Customer::getName)
				.orElse("Cliente desconhecido");
		return new RecentBudgetSummary(
				budget.getId(),
				budget.getCustomerId(),
				customerName,
				budget.getTotalAmount(),
				budget.getStatus(),
				budget.getCreatedAt());
	}

	private BigDecimal calculateAverageTicket(BigDecimal grossRevenue, long approvedCount) {
		if (approvedCount == 0) {
			return BigDecimal.ZERO;
		}
		return grossRevenue.divide(BigDecimal.valueOf(approvedCount), 2, RoundingMode.HALF_UP);
	}

	private double calculateConversionRate(long approvedCount, long rejectedCount) {
		long decided = approvedCount + rejectedCount;
		if (decided == 0) {
			return 0.0;
		}
		return BigDecimal.valueOf(approvedCount)
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(decided), 1, RoundingMode.HALF_UP)
				.doubleValue();
	}
}
