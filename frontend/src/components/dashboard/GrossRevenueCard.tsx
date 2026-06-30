const currencyFormatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

type GrossRevenueCardProps = {
  value: number | null;
  loading: boolean;
};

export function GrossRevenueCard({ value, loading }: GrossRevenueCardProps) {
  return (
    <div className="rounded-lg border border-border bg-card p-6 shadow-sm">
      <p className="text-sm font-medium text-muted-foreground">Faturamento Total Bruto</p>
      <p className="mt-2 text-3xl font-bold text-foreground">
        {loading ? "Carregando..." : value !== null ? currencyFormatter.format(value) : "—"}
      </p>
    </div>
  );
}
