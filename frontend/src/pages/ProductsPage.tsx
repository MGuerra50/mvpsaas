import { useCallback, useEffect, useState } from "react";

import { ProductFormDialog } from "@/components/products/ProductFormDialog";
import { ProductsPagination } from "@/components/products/ProductsPagination";
import { ProductsTable } from "@/components/products/ProductsTable";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useDebounce } from "@/hooks/useDebounce";
import {
  createProduct,
  listProducts,
  updateProduct,
  updateProductStatus,
  type Product,
  type ProductPage,
  type ProductPayload,
} from "@/lib/api/products";

const PAGE_SIZE = 10;

export function ProductsPage() {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [data, setData] = useState<ProductPage | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);

  const debouncedSearch = useDebounce(search, 300);

  const loadProducts = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await listProducts({
        page,
        size: PAGE_SIZE,
        search: debouncedSearch || undefined,
      });
      setData(result);
    } catch {
      setError("Não foi possível carregar os produtos.");
    } finally {
      setLoading(false);
    }
  }, [page, debouncedSearch]);

  useEffect(() => {
    loadProducts();
  }, [loadProducts]);

  useEffect(() => {
    setPage(0);
  }, [debouncedSearch]);

  function openCreateDialog() {
    setEditingProduct(null);
    setDialogOpen(true);
  }

  function openEditDialog(product: Product) {
    setEditingProduct(product);
    setDialogOpen(true);
  }

  async function handleFormSubmit(payload: ProductPayload) {
    try {
      if (editingProduct) {
        await updateProduct(editingProduct.id, payload);
      } else {
        await createProduct(payload);
      }
      await loadProducts();
    } catch {
      setError("Não foi possível salvar o produto. Verifique se o SKU já existe.");
      throw new Error("save failed");
    }
  }

  async function handleToggleStatus(product: Product) {
    const nextStatus = product.status === "ACTIVE" ? "INACTIVE" : "ACTIVE";
    const action = nextStatus === "INACTIVE" ? "inativar" : "ativar";
    const confirmed = window.confirm(`Deseja ${action} o produto "${product.name}"?`);
    if (!confirmed) return;

    try {
      await updateProductStatus(product.id, nextStatus);
      await loadProducts();
    } catch {
      setError(`Não foi possível ${action} o produto.`);
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">Produtos</h1>
          <p className="text-muted-foreground">Gerencie o catálogo de produtos do seu negócio.</p>
        </div>
        <Button onClick={openCreateDialog}>Novo produto</Button>
      </div>

      <Input
        placeholder="Buscar por nome ou SKU..."
        value={search}
        onChange={(event) => setSearch(event.target.value)}
        className="max-w-md"
      />

      {error && <p className="text-sm text-destructive">{error}</p>}

      <ProductsTable
        products={data?.content ?? []}
        loading={loading}
        onEdit={openEditDialog}
        onToggleStatus={handleToggleStatus}
      />

      <ProductsPagination
        page={data?.page ?? 0}
        totalPages={data?.totalPages ?? 0}
        totalElements={data?.totalElements ?? 0}
        onPageChange={setPage}
      />

      <ProductFormDialog
        open={dialogOpen}
        onOpenChange={setDialogOpen}
        product={editingProduct}
        onSubmit={handleFormSubmit}
      />
    </div>
  );
}
