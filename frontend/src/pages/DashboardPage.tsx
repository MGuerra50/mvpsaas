import { useEffect, useState } from "react";

import { BudgetStatusChart } from "@/components/dashboard/BudgetStatusChart";
import { DashboardKpiGrid } from "@/components/dashboard/DashboardKpiGrid";
import { MonthlyRevenueChart } from "@/components/dashboard/MonthlyRevenueChart";
import { RecentBudgetsTable } from "@/components/dashboard/RecentBudgetsTable";
import { TopCustomersChart } from "@/components/dashboard/TopCustomersChart";
import { fetchDashboardMetrics, type DashboardMetrics } from "@/lib/api/dashboard";

export function DashboardPage() {
  const [metrics, setMetrics] = useState<DashboardMetrics | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    async function load() {
      setLoading(true);
      setError(null);
      try {
        const data = await fetchDashboardMetrics();
        if (!cancelled) {
          setMetrics(data);
        }
      } catch {
        if (!cancelled) {
          setError("Não foi possível carregar as métricas do dashboard.");
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    load();
    return () => {
      cancelled = true;
    };
  }, []);

  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-2xl font-bold">Dashboard</h1>
        <p className="text-muted-foreground">Visão geral do faturamento e orçamentos do seu negócio.</p>
      </div>

      {error && <p className="text-sm text-destructive">{error}</p>}

      <DashboardKpiGrid metrics={metrics} loading={loading} />

      <div className="grid gap-6 lg:grid-cols-3">
        <div className="lg:col-span-2">
          <MonthlyRevenueChart data={metrics?.monthlyRevenue ?? []} loading={loading} />
        </div>
        <div>
          <BudgetStatusChart statusSummary={metrics?.statusSummary ?? null} loading={loading} />
        </div>
      </div>

      <div className="grid gap-6 lg:grid-cols-2">
        <TopCustomersChart data={metrics?.topCustomers ?? []} loading={loading} />
        <RecentBudgetsTable data={metrics?.recentBudgets ?? []} loading={loading} />
      </div>
    </div>
  );
}
