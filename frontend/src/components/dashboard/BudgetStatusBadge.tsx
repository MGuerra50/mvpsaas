import { cn } from "@/lib/utils";
import type { BudgetStatus } from "@/lib/api/dashboard";

const statusConfig: Record<BudgetStatus, { label: string; className: string }> = {
  DRAFT: {
    label: "Rascunho",
    className: "bg-amber-100 text-amber-800",
  },
  APPROVED: {
    label: "Aprovado",
    className: "bg-emerald-100 text-emerald-800",
  },
  REJECTED: {
    label: "Rejeitado",
    className: "bg-red-100 text-red-800",
  },
};

type BudgetStatusBadgeProps = {
  status: BudgetStatus;
};

export function BudgetStatusBadge({ status }: BudgetStatusBadgeProps) {
  const config = statusConfig[status];

  return (
    <span
      className={cn(
        "inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium",
        config.className,
      )}
    >
      {config.label}
    </span>
  );
}
