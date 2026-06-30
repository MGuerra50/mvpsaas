import { Button } from "@/components/ui/button";

type CustomersPaginationProps = {
  page: number;
  totalPages: number;
  totalElements: number;
  onPageChange: (page: number) => void;
};

export function CustomersPagination({
  page,
  totalPages,
  totalElements,
  onPageChange,
}: CustomersPaginationProps) {
  const currentPage = page + 1;

  return (
    <div className="flex items-center justify-between">
      <p className="text-sm text-muted-foreground">
        {totalElements} cliente{totalElements !== 1 ? "s" : ""} · Página {currentPage} de{" "}
        {Math.max(totalPages, 1)}
      </p>
      <div className="flex gap-2">
        <Button
          variant="outline"
          size="sm"
          disabled={page <= 0}
          onClick={() => onPageChange(page - 1)}
        >
          Anterior
        </Button>
        <Button
          variant="outline"
          size="sm"
          disabled={page >= totalPages - 1 || totalPages === 0}
          onClick={() => onPageChange(page + 1)}
        >
          Próxima
        </Button>
      </div>
    </div>
  );
}
