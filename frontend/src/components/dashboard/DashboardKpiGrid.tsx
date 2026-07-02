import type { ComponentType } from "react";
import { FileText, Percent, Receipt, TrendingUp } from "lucide-react";

import { Card, CardContent } from "@/components/ui/card";
import type { DashboardMetrics } from "@/lib/api/dashboard";
import { currencyFormatter, formatPercent } from "@/lib/format/currency";

type DashboardKpiGridProps = {
  metrics: DashboardMetrics | null;
  loading: boolean;
};

type KpiItem = {
  title: string;
  value: string;
  subtitle?: string;
  icon: ComponentType<{ className?: string }>;
};

export function DashboardKpiGrid({ metrics, loading }: DashboardKpiGridProps) {
  const items: KpiItem[] = [
    {
      title: "Faturamento bruto",
      value: metrics ? currencyFormatter.format(metrics.grossRevenue) : "—",
      icon: TrendingUp,
    },
    {
      title: "Rascunhos",
      value: metrics ? String(metrics.statusSummary.draft.count) : "—",
      subtitle: metrics ? currencyFormatter.format(metrics.statusSummary.draft.totalAmount) : undefined,
      icon: FileText,
    },
    {
      title: "Ticket médio",
      value: metrics ? currencyFormatter.format(metrics.averageTicket) : "—",
      icon: Receipt,
    },
    {
      title: "Taxa de conversão",
      value: metrics ? formatPercent(metrics.conversionRate) : "—",
      icon: Percent,
    },
  ];

  return (
    <div className="grid grid-cols-2 gap-4 lg:grid-cols-4">
      {items.map((item) => (
        <Card key={item.title}>
          <CardContent className="p-6">
            <div className="flex items-start justify-between gap-2">
              <div className="min-w-0">
                <p className="text-sm font-medium text-muted-foreground">{item.title}</p>
                <p className="mt-2 text-2xl font-bold text-foreground">
                  {loading ? "..." : item.value}
                </p>
                {item.subtitle && !loading && (
                  <p className="mt-1 text-xs text-muted-foreground">{item.subtitle}</p>
                )}
              </div>
              <item.icon className="h-5 w-5 shrink-0 text-[#F97316]" aria-hidden />
            </div>
          </CardContent>
        </Card>
      ))}
    </div>
  );
}
