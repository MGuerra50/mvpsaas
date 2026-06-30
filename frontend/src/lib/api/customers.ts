import api from "@/lib/api/client";

export type Customer = {
  id: number;
  tenantId: number;
  name: string;
  phone: string;
  documentId: string;
};

export type CustomerPage = {
  content: Customer[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
};

export type CustomerPayload = {
  name: string;
  phone: string;
  documentId: string;
};

export async function listCustomers(params: {
  page: number;
  size: number;
  search?: string;
}): Promise<CustomerPage> {
  const response = await api.get<CustomerPage>("/customers", { params });
  return response.data;
}

export async function createCustomer(data: CustomerPayload): Promise<Customer> {
  const response = await api.post<Customer>("/customers", data);
  return response.data;
}

export async function updateCustomer(id: number, data: CustomerPayload): Promise<Customer> {
  const response = await api.put<Customer>(`/customers/${id}`, data);
  return response.data;
}

export async function deleteCustomer(id: number): Promise<void> {
  await api.delete(`/customers/${id}`);
}
