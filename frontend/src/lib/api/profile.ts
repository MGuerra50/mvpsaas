import api from "@/lib/api/client";

export type UserProfile = {
  id: number;
  tenantId: number;
  name: string;
  email: string;
  profileImageUrl: string | null;
};

export type UpdateProfilePayload = {
  name: string;
  profileImageUrl: string | null;
};

export type ChangePasswordPayload = {
  currentPassword: string;
  newPassword: string;
};

export async function fetchProfile(): Promise<UserProfile> {
  const response = await api.get<UserProfile>("/auth/me");
  return response.data;
}

export async function updateProfile(data: UpdateProfilePayload): Promise<UserProfile> {
  const response = await api.put<UserProfile>("/auth/profile", data);
  return response.data;
}

export async function changePassword(data: ChangePasswordPayload): Promise<void> {
  await api.put("/auth/password", data);
}
