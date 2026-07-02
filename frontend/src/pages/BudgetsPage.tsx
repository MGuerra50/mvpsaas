import { useState } from "react";

import { BudgetBuilderLayout } from "@/components/budgets/BudgetBuilderLayout";
import { BudgetMirrorPanel } from "@/components/budgets/BudgetMirrorPanel";
import { CustomerSelect } from "@/components/budgets/CustomerSelect";
import { ProductSearchPanel } from "@/components/budgets/ProductSearchPanel";
import { createBudget, updateBudgetStatus } from "@/lib/api/budgets";
import type { Customer } from "@/lib/api/customers";
import type { Product } from "@/lib/api/products";
import {
  formatBudgetMessage,
  normalizePhoneForWhatsApp,
} from "@/lib/whatsapp/formatBudgetMessage";
import { grandTotal, type CartItem } from "@/types/budgetCart";

export function BudgetsPage() {
  const [selectedCustomer, setSelectedCustomer] = useState<Customer | null>(null);
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  function handleAddProduct(product: Product) {
    setCartItems((current) => {
      const existing = current.find((item) => item.productId === product.id);
      if (existing) {
        return current.map((item) =>
          item.productId === product.id ? { ...item, quantity: item.quantity + 1 } : item,
        );
      }
      return [
        ...current,
        {
          productId: product.id,
          name: product.name,
          unitPrice: product.unitPrice,
          quantity: 1,
          discountPercent: 0,
        },
      ];
    });
  }

  function handleQuantityChange(productId: number, quantity: number) {
    setCartItems((current) =>
      current.map((item) => (item.productId === productId ? { ...item, quantity } : item)),
    );
  }

  function handleDiscountChange(productId: number, discountPercent: number) {
    setCartItems((current) =>
      current.map((item) => (item.productId === productId ? { ...item, discountPercent } : item)),
    );
  }

  function handleRemove(productId: number) {
    setCartItems((current) => current.filter((item) => item.productId !== productId));
  }

  async function handleApproveAndSend() {
    setError(null);
    setSuccess(null);

    if (!selectedCustomer) {
      setError("Selecione um cliente antes de enviar.");
      return;
    }
    if (!selectedCustomer.phone?.trim()) {
      setError("O cliente selecionado não possui telefone cadastrado.");
      return;
    }
    if (cartItems.length === 0) {
      setError("Adicione ao menos um produto ao orçamento.");
      return;
    }

    setLoading(true);
    try {
      const total = grandTotal(cartItems);
      const budget = await createBudget({
        customerId: selectedCustomer.id,
        totalAmount: Number(total.toFixed(2)),
      });
      await updateBudgetStatus(budget.id, "APPROVED");

      const message = formatBudgetMessage({
        customerName: selectedCustomer.name,
        items: cartItems,
      });
      const phone = normalizePhoneForWhatsApp(selectedCustomer.phone);
      window.open(`https://wa.me/${phone}?text=${encodeURIComponent(message)}`, "_blank");

      setSuccess("Orçamento aprovado! WhatsApp aberto para envio.");
      setCartItems([]);
      setSelectedCustomer(null);
    } catch {
      setError("Não foi possível aprovar e enviar o orçamento.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Orçamentos</h1>
        <p className="text-muted-foreground">Monte o pedido e envie pelo WhatsApp em um clique.</p>
      </div>

      {error && <p className="text-sm text-destructive">{error}</p>}
      {success && <p className="text-sm text-green-600">{success}</p>}

      <BudgetBuilderLayout
        left={
          <>
            <CustomerSelect selected={selectedCustomer} onSelect={setSelectedCustomer} />
            <ProductSearchPanel onAddProduct={handleAddProduct} />
          </>
        }
        right={
          <BudgetMirrorPanel
            items={cartItems}
            loading={loading}
            onQuantityChange={handleQuantityChange}
            onDiscountChange={handleDiscountChange}
            onRemove={handleRemove}
            onApproveAndSend={handleApproveAndSend}
          />
        }
      />
    </div>
  );
}
