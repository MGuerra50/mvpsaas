import { MessageCircle, Trash2 } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  currencyFormatter,
  discountTotal,
  grandTotal,
  subtotal,
  type CartItem,
} from "@/types/budgetCart";

type BudgetMirrorPanelProps = {
  items: CartItem[];
  loading: boolean;
  onQuantityChange: (productId: number, quantity: number) => void;
  onDiscountChange: (productId: number, discountPercent: number) => void;
  onRemove: (productId: number) => void;
  onApproveAndSend: () => void;
};

export function BudgetMirrorPanel({
  items,
  loading,
  onQuantityChange,
  onDiscountChange,
  onRemove,
  onApproveAndSend,
}: BudgetMirrorPanelProps) {
  return (
    <div className="rounded-lg border border-border bg-card p-4 shadow-sm">
      <p className="mb-3 text-sm font-medium">Espelho do orçamento</p>

      {items.length === 0 ? (
        <p className="py-8 text-center text-sm text-muted-foreground">
          Adicione produtos para montar o orçamento.
        </p>
      ) : (
        <ul className="space-y-3">
          {items.map((item) => (
            <li key={item.productId} className="rounded-md border border-border p-3">
              <div className="flex items-start justify-between gap-2">
                <div>
                  <p className="font-medium">{item.name}</p>
                  <p className="text-xs text-muted-foreground">
                    Unit. {currencyFormatter.format(item.unitPrice)}
                  </p>
                </div>
                <Button
                  type="button"
                  variant="ghost"
                  size="sm"
                  onClick={() => onRemove(item.productId)}
                >
                  <Trash2 className="h-4 w-4" />
                </Button>
              </div>
              <div className="mt-2 grid grid-cols-2 gap-2">
                <div>
                  <label className="text-xs text-muted-foreground">Qtd</label>
                  <Input
                    type="number"
                    min={1}
                    value={item.quantity}
                    onChange={(event) =>
                      onQuantityChange(item.productId, Math.max(1, Number(event.target.value) || 1))
                    }
                  />
                </div>
                <div>
                  <label className="text-xs text-muted-foreground">Desc. (%)</label>
                  <Input
                    type="number"
                    min={0}
                    max={100}
                    value={item.discountPercent}
                    onChange={(event) =>
                      onDiscountChange(
                        item.productId,
                        Math.min(100, Math.max(0, Number(event.target.value) || 0)),
                      )
                    }
                  />
                </div>
              </div>
            </li>
          ))}
        </ul>
      )}

      <div className="mt-4 space-y-1 border-t border-border pt-4 text-sm">
        <div className="flex justify-between">
          <span className="text-muted-foreground">Subtotal</span>
          <span>{currencyFormatter.format(subtotal(items))}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-muted-foreground">Descontos</span>
          <span>- {currencyFormatter.format(discountTotal(items))}</span>
        </div>
        <div className="flex justify-between text-base font-bold">
          <span>Total</span>
          <span className="text-primary">{currencyFormatter.format(grandTotal(items))}</span>
        </div>
      </div>

      <Button
        type="button"
        className="mt-4 w-full gap-2"
        disabled={loading || items.length === 0}
        onClick={onApproveAndSend}
      >
        <MessageCircle className="h-4 w-4" />
        Aprovar e Enviar Instantaneamente via WhatsApp
      </Button>
    </div>
  );
}
