import { Plus } from "lucide-react";
import { useMemo, useState } from "react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { searchProducts, type Product } from "@/lib/data/products";
import { currencyFormatter } from "@/types/budgetCart";

type ProductSearchPanelProps = {
  onAddProduct: (product: Product) => void;
};

export function ProductSearchPanel({ onAddProduct }: ProductSearchPanelProps) {
  const [query, setQuery] = useState("");
  const results = useMemo(() => searchProducts(query), [query]);

  return (
    <div className="rounded-lg border border-border bg-card p-4 shadow-sm">
      <p className="mb-2 text-sm font-medium">Produtos</p>
      <Input
        placeholder="Buscar por nome ou SKU..."
        value={query}
        onChange={(event) => setQuery(event.target.value)}
      />
      <ul className="mt-3 max-h-72 space-y-2 overflow-y-auto">
        {results.map((product) => (
          <li
            key={product.id}
            className="flex items-center justify-between gap-2 rounded-md border border-border px-3 py-2"
          >
            <div className="min-w-0">
              <p className="truncate font-medium">{product.name}</p>
              <p className="text-xs text-muted-foreground">
                {product.sku} · {currencyFormatter.format(product.unitPrice)}
              </p>
            </div>
            <Button type="button" size="sm" variant="outline" onClick={() => onAddProduct(product)}>
              <Plus className="h-4 w-4" />
              Adicionar
            </Button>
          </li>
        ))}
      </ul>
    </div>
  );
}
