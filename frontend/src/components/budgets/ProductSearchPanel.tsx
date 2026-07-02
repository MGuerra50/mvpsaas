import { Plus } from "lucide-react";
import { useEffect, useMemo, useState } from "react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { listProducts, type Product } from "@/lib/api/products";
import { currencyFormatter } from "@/lib/format/currency";

type ProductSearchPanelProps = {
  onAddProduct: (product: Product) => void;
};

export function ProductSearchPanel({ onAddProduct }: ProductSearchPanelProps) {
  const [query, setQuery] = useState("");
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    async function load() {
      setLoading(true);
      setError(null);
      try {
        const result = await listProducts({ page: 0, size: 100, status: "ACTIVE" });
        if (!cancelled) {
          setProducts(result.content);
        }
      } catch {
        if (!cancelled) {
          setError("Não foi possível carregar os produtos.");
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    load();
    return () => {
      cancelled = true;
    };
  }, []);

  const results = useMemo(() => {
    const normalized = query.trim().toLowerCase();
    if (!normalized) return products;
    return products.filter(
      (product) =>
        product.name.toLowerCase().includes(normalized) ||
        product.sku.toLowerCase().includes(normalized),
    );
  }, [products, query]);

  return (
    <div className="rounded-lg border border-border bg-card p-4 shadow-sm">
      <p className="mb-2 text-sm font-medium">Produtos</p>
      <Input
        placeholder="Buscar por nome ou SKU..."
        value={query}
        onChange={(event) => setQuery(event.target.value)}
      />
      {error && <p className="mt-2 text-xs text-destructive">{error}</p>}
      {loading ? (
        <p className="mt-3 text-sm text-muted-foreground">Carregando produtos...</p>
      ) : (
        <ul className="mt-3 max-h-72 space-y-2 overflow-y-auto">
          {results.length === 0 ? (
            <li className="px-3 py-2 text-sm text-muted-foreground">Nenhum produto ativo encontrado.</li>
          ) : (
            results.map((product) => (
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
            ))
          )}
        </ul>
      )}
    </div>
  );
}
