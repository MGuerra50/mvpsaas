export type Product = {
  id: number;
  name: string;
  sku: string;
  unitPrice: number;
};

const CATALOG: Product[] = [
  { id: 1, name: "Notebook Pro 15\"", sku: "NB-001", unitPrice: 4599.9 },
  { id: 2, name: "Mouse Ergonômico", sku: "MS-002", unitPrice: 89.9 },
  { id: 3, name: "Teclado Mecânico RGB", sku: "KB-003", unitPrice: 349.0 },
  { id: 4, name: "Monitor 27\" 4K", sku: "MN-004", unitPrice: 1899.0 },
  { id: 5, name: "Webcam Full HD", sku: "WC-005", unitPrice: 249.9 },
  { id: 6, name: "Headset Bluetooth", sku: "HS-006", unitPrice: 299.0 },
  { id: 7, name: "Suporte para Notebook", sku: "ST-007", unitPrice: 129.9 },
  { id: 8, name: "Hub USB-C 7 portas", sku: "HB-008", unitPrice: 179.0 },
  { id: 9, name: "SSD 1TB NVMe", sku: "SD-009", unitPrice: 429.9 },
  { id: 10, name: "Memória RAM 16GB", sku: "RM-010", unitPrice: 319.0 },
  { id: 11, name: "Impressora Laser", sku: "PR-011", unitPrice: 1299.0 },
  { id: 12, name: "Roteador Wi-Fi 6", sku: "RT-012", unitPrice: 549.0 },
  { id: 13, name: "Cadeira Ergonômica", sku: "CH-013", unitPrice: 1599.0 },
  { id: 14, name: "Mesa Ajustável", sku: "DK-014", unitPrice: 2199.0 },
  { id: 15, name: "Licença Office 365", sku: "SW-015", unitPrice: 399.0 },
];

export function searchProducts(query: string): Product[] {
  const normalized = query.trim().toLowerCase();
  if (!normalized) return CATALOG;
  return CATALOG.filter(
    (product) =>
      product.name.toLowerCase().includes(normalized) ||
      product.sku.toLowerCase().includes(normalized),
  );
}

export function getProductById(id: number): Product | undefined {
  return CATALOG.find((product) => product.id === id);
}
