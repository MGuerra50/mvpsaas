import {
  Bar,
  BarChart,
  CartesianGrid,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

import { DashboardEmptyState } from "@/components/dashboard/DashboardEmptyState";
import type { TopCustomer } from "@/lib/api/dashboard";
import { currencyFormatter } from "@/lib/format/currency";

type TopCustomersChartProps = {
  data: TopCustomer[];
  loading: boolean;
};

export function TopCustomersChart({ data, loading }: TopCustomersChartProps) {
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
        message="Nenhum cliente com orçamentos aprovados ainda."
      />
    );
  }

  const chartData = data.map((customer) => ({
    name: customer.customerName,
    totalAmount: customer.totalAmount,
    budgetCount: customer.budgetCount,
  }));

  return (
    <div className="rounded-lg border border-border bg-card p-6 shadow-sm">
      <h2 className="mb-4 text-lg font-semibold">Top clientes</h2>
      <ResponsiveContainer width="100%" height={320}>
        <BarChart data={chartData} layout="vertical" margin={{ left: 20, right: 20 }}>
          <CartesianGrid strokeDasharray="3 3" horizontal={false} />
          <XAxis
            type="number"
            tickFormatter={(value) => currencyFormatter.format(Number(value))}
          />
          <YAxis type="category" dataKey="name" width={120} tick={{ fontSize: 12 }} />
          <Tooltip
            formatter={(value: number, _name, props) => {
              const count = props.payload.budgetCount as number;
              return [currencyFormatter.format(value), `${count} orçamento(s)`];
            }}
          />
          <Bar dataKey="totalAmount" fill="#F97316" radius={[0, 4, 4, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}
