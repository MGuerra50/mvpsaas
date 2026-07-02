import { Link } from "react-router-dom";

import { Button } from "@/components/ui/button";

type DashboardEmptyStateProps = {
  message: string;
  className?: string;
};

export function DashboardEmptyState({ message, className }: DashboardEmptyStateProps) {
  return (
    <div
      className={`flex flex-col items-center justify-center gap-3 rounded-lg border border-border bg-card p-8 text-center text-muted-foreground ${className ?? ""}`}
    >
      <p className="text-sm">{message}</p>
      <Button asChild variant="outline" size="sm">
        <Link to="/budgets">Criar orçamento</Link>
      </Button>
    </div>
  );
}
