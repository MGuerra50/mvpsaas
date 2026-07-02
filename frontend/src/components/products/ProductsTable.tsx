import {
  flexRender,
  getCoreRowModel,
  useReactTable,
  type ColumnDef,
} from "@tanstack/react-table";
import { Pencil, Power, PowerOff } from "lucide-react";
import { useMemo } from "react";

import { ProductStatusBadge } from "@/components/products/ProductStatusBadge";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import type { Product } from "@/lib/api/products";
import { currencyFormatter } from "@/lib/format/currency";

type ProductsTableProps = {
  products: Product[];
  loading: boolean;
  onEdit: (product: Product) => void;
  onToggleStatus: (product: Product) => void;
};

export function ProductsTable({ products, loading, onEdit, onToggleStatus }: ProductsTableProps) {
  const columns = useMemo<ColumnDef<Product>[]>(
    () => [
      { accessorKey: "name", header: "Nome" },
      { accessorKey: "sku", header: "SKU" },
      {
        accessorKey: "unitPrice",
        header: "Preço",
        cell: ({ row }) => currencyFormatter.format(row.original.unitPrice),
      },
      {
        accessorKey: "status",
        header: "Status",
        cell: ({ row }) => <ProductStatusBadge status={row.original.status} />,
      },
      {
        id: "actions",
        header: "Ações",
        cell: ({ row }) => {
          const product = row.original;
          const isActive = product.status === "ACTIVE";
          return (
            <div className="flex gap-2">
              <Button variant="outline" size="sm" onClick={() => onEdit(product)}>
                <Pencil className="h-4 w-4" />
              </Button>
              <Button
                variant="outline"
                size="sm"
                onClick={() => onToggleStatus(product)}
                title={isActive ? "Inativar produto" : "Ativar produto"}
              >
                {isActive ? <PowerOff className="h-4 w-4" /> : <Power className="h-4 w-4" />}
              </Button>
            </div>
          );
        },
      },
    ],
    [onEdit, onToggleStatus],
  );

  const table = useReactTable({
    data: products,
    columns,
    getCoreRowModel: getCoreRowModel(),
  });

  if (loading) {
    return (
      <div className="rounded-lg border border-border bg-card p-8 text-center text-muted-foreground">
        Carregando produtos...
      </div>
    );
  }

  return (
    <div className="rounded-lg border border-border bg-card">
      <Table>
        <TableHeader>
          {table.getHeaderGroups().map((headerGroup) => (
            <TableRow key={headerGroup.id}>
              {headerGroup.headers.map((header) => (
                <TableHead key={header.id}>
                  {header.isPlaceholder
                    ? null
                    : flexRender(header.column.columnDef.header, header.getContext())}
                </TableHead>
              ))}
            </TableRow>
          ))}
        </TableHeader>
        <TableBody>
          {table.getRowModel().rows.length === 0 ? (
            <TableRow>
              <TableCell colSpan={columns.length} className="h-24 text-center text-muted-foreground">
                Nenhum produto encontrado.
              </TableCell>
            </TableRow>
          ) : (
            table.getRowModel().rows.map((row) => (
              <TableRow key={row.id}>
                {row.getVisibleCells().map((cell) => (
                  <TableCell key={cell.id}>
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </TableCell>
                ))}
              </TableRow>
            ))
          )}
        </TableBody>
      </Table>
    </div>
  );
}
