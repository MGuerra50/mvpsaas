import {
  Bar,
  BarChart,
  CartesianGrid,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

import type { MonthlyRevenue } from "@/lib/api/dashboard";

const currencyFormatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

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
      <div className="flex h-80 items-center justify-center rounded-lg border border-border bg-card text-muted-foreground">
        Nenhum dado de faturamento aprovado ainda.
      </div>
    );
  }

  return (
    <div className="rounded-lg border border-border bg-card p-6 shadow-sm">
      <h2 className="mb-4 text-lg font-semibold">Faturamento por mês</h2>
      <ResponsiveContainer width="100%" height={320}>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" vertical={false} />
          <XAxis dataKey="month" />
          <YAxis tickFormatter={(value) => currencyFormatter.format(Number(value))} width={100} />
          <Tooltip
            formatter={(value: number) => currencyFormatter.format(value)}
            labelFormatter={(label) => `Mês: ${label}`}
          />
          <Bar dataKey="amount" fill="#F97316" radius={[4, 4, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}
