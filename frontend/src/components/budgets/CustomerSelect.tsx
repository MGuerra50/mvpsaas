import { useEffect, useState } from "react";

import { Input } from "@/components/ui/input";
import { useDebounce } from "@/hooks/useDebounce";
import { listCustomers, type Customer } from "@/lib/api/customers";
import { cn } from "@/lib/utils";

type CustomerSelectProps = {
  selected: Customer | null;
  onSelect: (customer: Customer | null) => void;
};

export function CustomerSelect({ selected, onSelect }: CustomerSelectProps) {
  const [search, setSearch] = useState("");
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(false);
  const debouncedSearch = useDebounce(search, 300);

  useEffect(() => {
    let cancelled = false;

    async function load() {
      setLoading(true);
      try {
        const result = await listCustomers({
          page: 0,
          size: 10,
          search: debouncedSearch || undefined,
        });
        if (!cancelled) {
          setCustomers(result.content);
        }
      } catch {
        if (!cancelled) {
          setCustomers([]);
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
  }, [debouncedSearch]);

  return (
    <div className="rounded-lg border border-border bg-card p-4 shadow-sm">
      <p className="mb-2 text-sm font-medium">Cliente</p>
      {selected ? (
        <div className="flex items-center justify-between rounded-md border border-border bg-muted/50 px-3 py-2">
          <div>
            <p className="font-medium">{selected.name}</p>
            <p className="text-xs text-muted-foreground">{selected.phone}</p>
          </div>
          <button
            type="button"
            className="text-sm text-primary hover:underline"
            onClick={() => onSelect(null)}
          >
            Trocar
          </button>
        </div>
      ) : (
        <>
          <Input
            placeholder="Buscar cliente por nome ou telefone..."
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
          <ul className="mt-2 max-h-40 space-y-1 overflow-y-auto">
            {loading && <li className="px-2 py-1 text-sm text-muted-foreground">Carregando...</li>}
            {!loading && customers.length === 0 && (
              <li className="px-2 py-1 text-sm text-muted-foreground">Nenhum cliente encontrado.</li>
            )}
            {customers.map((customer) => (
              <li key={customer.id}>
                <button
                  type="button"
                  className={cn(
                    "w-full rounded-md px-2 py-2 text-left text-sm hover:bg-muted",
                  )}
                  onClick={() => {
                    onSelect(customer);
                    setSearch("");
                  }}
                >
                  <span className="font-medium">{customer.name}</span>
                  <span className="ml-2 text-muted-foreground">{customer.phone}</span>
                </button>
              </li>
            ))}
          </ul>
        </>
      )}
    </div>
  );
}
