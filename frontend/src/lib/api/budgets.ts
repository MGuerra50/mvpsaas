import api from "@/lib/api/client";

export type BudgetStatus = "DRAFT" | "APPROVED" | "REJECTED";

export type Budget = {
  id: number;
  tenantId: number;
  customerId: number;
  totalAmount: number;
  status: BudgetStatus;
  createdAt: string;
};

export type CreateBudgetPayload = {
  customerId: number;
  totalAmount: number;
};

export async function listBudgets(status?: BudgetStatus): Promise<Budget[]> {
  const response = await api.get<Budget[]>("/budgets", {
    params: status ? { status } : undefined,
  });
  return response.data;
}

export async function createBudget(data: CreateBudgetPayload): Promise<Budget> {
  const response = await api.post<Budget>("/budgets", data);
  return response.data;
}

export async function updateBudgetStatus(
  id: number,
  status: "APPROVED" | "REJECTED",
): Promise<Budget> {
  const response = await api.patch<Budget>(`/budgets/${id}/status`, { status });
  return response.data;
}
