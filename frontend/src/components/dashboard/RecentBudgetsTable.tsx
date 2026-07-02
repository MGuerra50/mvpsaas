import { BudgetStatusBadge } from "@/components/dashboard/BudgetStatusBadge";
import { DashboardEmptyState } from "@/components/dashboard/DashboardEmptyState";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import type { RecentBudget } from "@/lib/api/dashboard";
import { currencyFormatter } from "@/lib/format/currency";

type RecentBudgetsTableProps = {
  data: RecentBudget[];
  loading: boolean;
};

const dateFormatter = new Intl.DateTimeFormat("pt-BR", {
  dateStyle: "short",
  timeStyle: "short",
});

export function RecentBudgetsTable({ data, loading }: RecentBudgetsTableProps) {
  if (loading) {
    return (
      <div className="flex h-80 items-center justify-center rounded-lg border border-border bg-card text-muted-foreground">
        Carregando orçamentos...
      </div>
    );
  }

  if (data.length === 0) {
    return (
      <DashboardEmptyState
        className="h-80"
        message="Nenhum orçamento recente para exibir."
      />
    );
  }

  return (
    <div className="rounded-lg border border-border bg-card p-6 shadow-sm">
      <h2 className="mb-4 text-lg font-semibold">Últimos orçamentos</h2>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Cliente</TableHead>
            <TableHead>Valor</TableHead>
            <TableHead>Status</TableHead>
            <TableHead>Criado em</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {data.map((budget) => (
            <TableRow key={budget.id}>
              <TableCell className="font-medium">{budget.customerName}</TableCell>
              <TableCell>{currencyFormatter.format(budget.totalAmount)}</TableCell>
              <TableCell>
                <BudgetStatusBadge status={budget.status} />
              </TableCell>
              <TableCell className="text-muted-foreground">
                {dateFormatter.format(new Date(budget.createdAt))}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}
