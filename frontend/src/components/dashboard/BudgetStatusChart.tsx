import { Cell, Pie, PieChart, ResponsiveContainer, Tooltip } from "recharts";

import { DashboardEmptyState } from "@/components/dashboard/DashboardEmptyState";
import type { StatusSummary } from "@/lib/api/dashboard";
import { currencyFormatter } from "@/lib/format/currency";

type BudgetStatusChartProps = {
  statusSummary: StatusSummary | null;
  loading: boolean;
};

const COLORS = {
  draft: "#FBBF24",
  approved: "#22C55E",
  rejected: "#EF4444",
};

export function BudgetStatusChart({ statusSummary, loading }: BudgetStatusChartProps) {
  if (loading) {
    return (
      <div className="flex h-80 items-center justify-center rounded-lg border border-border bg-card text-muted-foreground">
        Carregando gráfico...
      </div>
    );
  }

  if (!statusSummary) {
    return null;
  }

  const chartData = [
    { name: "Rascunhos", key: "draft", value: statusSummary.draft.count, amount: statusSummary.draft.totalAmount },
    { name: "Aprovados", key: "approved", value: statusSummary.approved.count, amount: statusSummary.approved.totalAmount },
    { name: "Rejeitados", key: "rejected", value: statusSummary.rejected.count, amount: statusSummary.rejected.totalAmount },
  ].filter((item) => item.value > 0);

  if (chartData.length === 0) {
    return (
      <DashboardEmptyState
        className="h-80"
        message="Nenhum orçamento registrado ainda."
      />
    );
  }

  return (
    <div className="rounded-lg border border-border bg-card p-6 shadow-sm">
      <h2 className="mb-4 text-lg font-semibold">Orçamentos por status</h2>
      <ResponsiveContainer width="100%" height={320}>
        <PieChart>
          <Pie
            data={chartData}
            dataKey="value"
            nameKey="name"
            cx="50%"
            cy="50%"
            innerRadius={70}
            outerRadius={110}
            paddingAngle={2}
          >
            {chartData.map((entry) => (
              <Cell key={entry.key} fill={COLORS[entry.key as keyof typeof COLORS]} />
            ))}
          </Pie>
          <Tooltip
            formatter={(value: number, _name, props) => {
              const amount = props.payload.amount as number;
              return [`${value} (${currencyFormatter.format(amount)})`, props.payload.name];
            }}
          />
        </PieChart>
      </ResponsiveContainer>
      <div className="mt-2 flex flex-wrap justify-center gap-4 text-xs text-muted-foreground">
        {chartData.map((item) => (
          <span key={item.key} className="flex items-center gap-1.5">
            <span
              className="inline-block h-2.5 w-2.5 rounded-full"
              style={{ backgroundColor: COLORS[item.key as keyof typeof COLORS] }}
            />
            {item.name}: {item.value}
          </span>
        ))}
      </div>
    </div>
  );
}
