import api from "@/lib/api/client";

export type MonthlyRevenue = {
  month: string;
  amount: number;
};

export type DashboardMetrics = {
  grossRevenue: number;
  monthlyRevenue: MonthlyRevenue[];
};

export async function fetchDashboardMetrics(): Promise<DashboardMetrics> {
  const response = await api.get<DashboardMetrics>("/dashboard/metrics");
  return response.data;
}
