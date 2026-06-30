export type CartItem = {
  productId: number;
  name: string;
  unitPrice: number;
  quantity: number;
  discountPercent: number;
};

export function lineSubtotal(item: CartItem): number {
  return item.unitPrice * item.quantity;
}

export function lineDiscount(item: CartItem): number {
  return lineSubtotal(item) * (item.discountPercent / 100);
}

export function lineTotal(item: CartItem): number {
  return lineSubtotal(item) - lineDiscount(item);
}

export function subtotal(items: CartItem[]): number {
  return items.reduce((sum, item) => sum + lineSubtotal(item), 0);
}

export function discountTotal(items: CartItem[]): number {
  return items.reduce((sum, item) => sum + lineDiscount(item), 0);
}

export function grandTotal(items: CartItem[]): number {
  return items.reduce((sum, item) => sum + lineTotal(item), 0);
}

export const currencyFormatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});
