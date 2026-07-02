import api from "@/lib/api/client";

export type MonthlyRevenue = {
  month: string;
  amount: number;
};

export type StatusMetric = {
  count: number;
  totalAmount: number;
};

export type StatusSummary = {
  draft: StatusMetric;
  approved: StatusMetric;
  rejected: StatusMetric;
};

export type TopCustomer = {
  customerId: number;
  customerName: string;
  totalAmount: number;
  budgetCount: number;
};

export type BudgetStatus = "DRAFT" | "APPROVED" | "REJECTED";

export type RecentBudget = {
  id: number;
  customerId: number;
  customerName: string;
  totalAmount: number;
  status: BudgetStatus;
  createdAt: string;
};

export type DashboardMetrics = {
  grossRevenue: number;
  monthlyRevenue: MonthlyRevenue[];
  statusSummary: StatusSummary;
  averageTicket: number;
  conversionRate: number;
  topCustomers: TopCustomer[];
  recentBudgets: RecentBudget[];
};

export async function fetchDashboardMetrics(): Promise<DashboardMetrics> {
  const response = await api.get<DashboardMetrics>("/dashboard/metrics");
  return response.data;
}
