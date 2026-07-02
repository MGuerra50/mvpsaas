import { cn } from "@/lib/utils";
import type { ProductStatus } from "@/lib/api/products";

const statusConfig: Record<ProductStatus, { label: string; className: string }> = {
  ACTIVE: {
    label: "Ativo",
    className: "bg-emerald-100 text-emerald-800",
  },
  INACTIVE: {
    label: "Inativo",
    className: "bg-slate-100 text-slate-600",
  },
};

type ProductStatusBadgeProps = {
  status: ProductStatus;
};

export function ProductStatusBadge({ status }: ProductStatusBadgeProps) {
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
