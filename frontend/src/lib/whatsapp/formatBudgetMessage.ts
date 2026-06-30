import { type CartItem, currencyFormatter, discountTotal, grandTotal, lineTotal, subtotal } from "@/types/budgetCart";

type FormatBudgetMessageParams = {
  customerName: string;
  items: CartItem[];
};

export function formatBudgetMessage({ customerName, items }: FormatBudgetMessageParams): string {
  const lines = items.map((item) => {
    const discountLabel = item.discountPercent > 0 ? ` (desc. ${item.discountPercent}%)` : "";
    return `• ${item.name} — ${item.quantity}x ${currencyFormatter.format(item.unitPrice)}${discountLabel} = ${currencyFormatter.format(lineTotal(item))}`;
  });

  return [
    `Olá, ${customerName}! Segue seu orçamento OrçaZap:`,
    "",
    ...lines,
    "",
    `Subtotal: ${currencyFormatter.format(subtotal(items))}`,
    `Desconto: ${currencyFormatter.format(discountTotal(items))}`,
    `Total: ${currencyFormatter.format(grandTotal(items))}`,
    "",
    "Aguardo sua confirmação!",
  ].join("\n");
}

export function normalizePhoneForWhatsApp(phone: string): string {
  const digits = phone.replace(/\D/g, "");
  if (digits.startsWith("55") && digits.length >= 12) {
    return digits;
  }
  if (digits.length >= 10 && digits.length <= 11) {
    return `55${digits}`;
  }
  return digits;
}
