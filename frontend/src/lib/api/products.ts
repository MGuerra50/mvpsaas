import api from "@/lib/api/client";

export type ProductStatus = "ACTIVE" | "INACTIVE";

export type Product = {
  id: number;
  tenantId: number;
  name: string;
  sku: string;
  unitPrice: number;
  status: ProductStatus;
};

export type ProductPage = {
  content: Product[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
};

export type ProductPayload = {
  name: string;
  sku: string;
  unitPrice: number;
};

export async function listProducts(params: {
  page: number;
  size: number;
  search?: string;
  status?: ProductStatus;
}): Promise<ProductPage> {
  const response = await api.get<ProductPage>("/products", { params });
  return response.data;
}

export async function createProduct(data: ProductPayload): Promise<Product> {
  const response = await api.post<Product>("/products", data);
  return response.data;
}

export async function updateProduct(id: number, data: ProductPayload): Promise<Product> {
  const response = await api.put<Product>(`/products/${id}`, data);
  return response.data;
}

export async function updateProductStatus(id: number, status: ProductStatus): Promise<Product> {
  const response = await api.patch<Product>(`/products/${id}/status`, { status });
  return response.data;
}
