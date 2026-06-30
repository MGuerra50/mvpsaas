import { useCallback, useEffect, useState } from "react";

import { CustomerFormDialog } from "@/components/customers/CustomerFormDialog";
import { CustomersPagination } from "@/components/customers/CustomersPagination";
import { CustomersTable } from "@/components/customers/CustomersTable";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useDebounce } from "@/hooks/useDebounce";
import {
  createCustomer,
  deleteCustomer,
  listCustomers,
  updateCustomer,
  type Customer,
  type CustomerPage,
  type CustomerPayload,
} from "@/lib/api/customers";

const PAGE_SIZE = 10;

export function CustomersPage() {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [data, setData] = useState<CustomerPage | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingCustomer, setEditingCustomer] = useState<Customer | null>(null);

  const debouncedSearch = useDebounce(search, 300);

  const loadCustomers = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await listCustomers({
        page,
        size: PAGE_SIZE,
        search: debouncedSearch || undefined,
      });
      setData(result);
    } catch {
      setError("Não foi possível carregar os clientes.");
    } finally {
      setLoading(false);
    }
  }, [page, debouncedSearch]);

  useEffect(() => {
    loadCustomers();
  }, [loadCustomers]);

  useEffect(() => {
    setPage(0);
  }, [debouncedSearch]);

  function openCreateDialog() {
    setEditingCustomer(null);
    setDialogOpen(true);
  }

  function openEditDialog(customer: Customer) {
    setEditingCustomer(customer);
    setDialogOpen(true);
  }

  async function handleFormSubmit(payload: CustomerPayload) {
    if (editingCustomer) {
      await updateCustomer(editingCustomer.id, payload);
    } else {
      await createCustomer(payload);
    }
    await loadCustomers();
  }

  async function handleDelete(customer: Customer) {
    const confirmed = window.confirm(`Excluir o cliente "${customer.name}"?`);
    if (!confirmed) return;
    await deleteCustomer(customer.id);
    await loadCustomers();
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">Clientes</h1>
          <p className="text-muted-foreground">Gerencie sua base de clientes (CRM).</p>
        </div>
        <Button onClick={openCreateDialog}>Novo cliente</Button>
      </div>

      <Input
        placeholder="Buscar por nome, telefone ou documento..."
        value={search}
        onChange={(event) => setSearch(event.target.value)}
        className="max-w-md"
      />

      {error && <p className="text-sm text-destructive">{error}</p>}

      <CustomersTable
        customers={data?.content ?? []}
        loading={loading}
        onEdit={openEditDialog}
        onDelete={handleDelete}
      />

      <CustomersPagination
        page={data?.page ?? 0}
        totalPages={data?.totalPages ?? 0}
        totalElements={data?.totalElements ?? 0}
        onPageChange={setPage}
      />

      <CustomerFormDialog
        open={dialogOpen}
        onOpenChange={setDialogOpen}
        customer={editingCustomer}
        onSubmit={handleFormSubmit}
      />
    </div>
  );
}
