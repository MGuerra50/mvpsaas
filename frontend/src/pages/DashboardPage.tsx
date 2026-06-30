import { useEffect, useState } from "react";

import { GrossRevenueCard } from "@/components/dashboard/GrossRevenueCard";
import { MonthlyRevenueChart } from "@/components/dashboard/MonthlyRevenueChart";
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
        <p className="text-muted-foreground">Visão geral do faturamento do seu negócio.</p>
      </div>

      {error && <p className="text-sm text-destructive">{error}</p>}

      <GrossRevenueCard value={metrics?.grossRevenue ?? null} loading={loading} />

      <MonthlyRevenueChart data={metrics?.monthlyRevenue ?? []} loading={loading} />
    </div>
  );
}
