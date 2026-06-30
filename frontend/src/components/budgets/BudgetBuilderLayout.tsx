import type { ReactNode } from "react";

type BudgetBuilderLayoutProps = {
  left: ReactNode;
  right: ReactNode;
};

export function BudgetBuilderLayout({ left, right }: BudgetBuilderLayoutProps) {
  return (
    <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
      <div className="space-y-4">{left}</div>
      <div className="space-y-4">{right}</div>
    </div>
  );
}
