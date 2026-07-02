package com.saas.b2b.budget.adapter.in.web;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.saas.b2b.budget.application.port.in.GetDashboardMetricsUseCase;
import com.saas.b2b.budget.domain.model.BudgetStatus;
import com.saas.b2b.budget.domain.model.BudgetStatusMetric;
import com.saas.b2b.budget.domain.model.DashboardStatusSummary;
import com.saas.b2b.budget.domain.model.MonthlyRevenue;
import com.saas.b2b.budget.domain.model.RecentBudgetSummary;
import com.saas.b2b.budget.domain.model.TopCustomerRevenue;

public record DashboardMetricsResponse(
		BigDecimal grossRevenue,
		List<MonthlyRevenueResponse> monthlyRevenue,
		StatusSummaryResponse statusSummary,
		BigDecimal averageTicket,
		double conversionRate,
		List<TopCustomerResponse> topCustomers,
		List<RecentBudgetResponse> recentBudgets) {

	public static DashboardMetricsResponse from(GetDashboardMetricsUseCase.DashboardMetrics metrics) {
		return new DashboardMetricsResponse(
				metrics.grossRevenue(),
				metrics.monthlyRevenue().stream().map(MonthlyRevenueResponse::from).toList(),
				StatusSummaryResponse.from(metrics.statusSummary()),
				metrics.averageTicket(),
				metrics.conversionRate(),
				metrics.topCustomers().stream().map(TopCustomerResponse::from).toList(),
				metrics.recentBudgets().stream().map(RecentBudgetResponse::from).toList());
	}

	public record MonthlyRevenueResponse(String month, BigDecimal amount) {
		public static MonthlyRevenueResponse from(MonthlyRevenue revenue) {
			return new MonthlyRevenueResponse(revenue.month(), revenue.amount());
		}
	}

	public record StatusMetricResponse(long count, BigDecimal totalAmount) {
		public static StatusMetricResponse from(BudgetStatusMetric metric) {
			return new StatusMetricResponse(metric.count(), metric.totalAmount());
		}
	}

	public record StatusSummaryResponse(
			StatusMetricResponse draft,
			StatusMetricResponse approved,
			StatusMetricResponse rejected) {

		public static StatusSummaryResponse from(DashboardStatusSummary summary) {
			return new StatusSummaryResponse(
					StatusMetricResponse.from(summary.draft()),
					StatusMetricResponse.from(summary.approved()),
					StatusMetricResponse.from(summary.rejected()));
		}
	}

	public record TopCustomerResponse(
			Long customerId,
			String customerName,
			BigDecimal totalAmount,
			long budgetCount) {

		public static TopCustomerResponse from(TopCustomerRevenue customer) {
			return new TopCustomerResponse(
					customer.customerId(),
					customer.customerName(),
					customer.totalAmount(),
					customer.budgetCount());
		}
	}

	public record RecentBudgetResponse(
			Long id,
			Long customerId,
			String customerName,
			BigDecimal totalAmount,
			BudgetStatus status,
			Instant createdAt) {

		public static RecentBudgetResponse from(RecentBudgetSummary budget) {
			return new RecentBudgetResponse(
					budget.id(),
					budget.customerId(),
					budget.customerName(),
					budget.totalAmount(),
					budget.status(),
					budget.createdAt());
		}
	}
}
