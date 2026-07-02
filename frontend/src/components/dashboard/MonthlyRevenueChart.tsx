import {
  Area,
  AreaChart,
  CartesianGrid,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

import { DashboardEmptyState } from "@/components/dashboard/DashboardEmptyState";
import type { MonthlyRevenue } from "@/lib/api/dashboard";
import { currencyFormatter } from "@/lib/format/currency";

type MonthlyRevenueChartProps = {
  data: MonthlyRevenue[];
  loading: boolean;
};

export function MonthlyRevenueChart({ data, loading }: MonthlyRevenueChartProps) {
  if (loading) {
    return (
      <div className="flex h-80 items-center justify-center rounded-lg border border-border bg-card text-muted-foreground">
        Carregando gráfico...
      </div>
    );
  }

  if (data.length === 0) {
    return (
      <DashboardEmptyState
        className="h-80"
        message="Nenhum dado de faturamento aprovado ainda."
      />
    );
  }

  return (
    <div className="rounded-lg border border-border bg-card p-6 shadow-sm">
      <h2 className="mb-4 text-lg font-semibold">Faturamento por mês</h2>
      <ResponsiveContainer width="100%" height={320}>
        <AreaChart data={data}>
          <defs>
            <linearGradient id="revenueGradient" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="#F97316" stopOpacity={0.35} />
              <stop offset="95%" stopColor="#F97316" stopOpacity={0} />
            </linearGradient>
          </defs>
          <CartesianGrid strokeDasharray="3 3" vertical={false} />
          <XAxis dataKey="month" />
          <YAxis tickFormatter={(value) => currencyFormatter.format(Number(value))} width={100} />
          <Tooltip
            formatter={(value: number) => currencyFormatter.format(value)}
            labelFormatter={(label) => `Mês: ${label}`}
          />
          <Area
            type="monotone"
            dataKey="amount"
            stroke="#F97316"
            strokeWidth={2}
            fill="url(#revenueGradient)"
          />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  );
}
