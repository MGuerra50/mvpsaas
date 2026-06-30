import axios, { type AxiosError, type InternalAxiosRequestConfig } from "axios";

import { tokenStore } from "@/lib/auth/tokenStore";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081/api",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = tokenStore.get();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

let refreshPromise: Promise<string | null> | null = null;

async function refreshAccessToken(): Promise<string | null> {
  if (!refreshPromise) {
    refreshPromise = api
      .post<{ accessToken: string }>("/auth/refresh")
      .then((response) => {
        const token = response.data.accessToken;
        tokenStore.set(token);
        return token;
      })
      .catch(() => {
        tokenStore.clear();
        tokenStore.notifyUnauthorized();
        return null;
      })
      .finally(() => {
        refreshPromise = null;
      });
  }
  return refreshPromise;
}

api.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };
    if (error.response?.status === 401 && originalRequest && !originalRequest._retry) {
      const url = originalRequest.url ?? "";
      if (url.includes("/auth/login") || url.includes("/auth/refresh") || url.includes("/auth/logout")) {
        return Promise.reject(error);
      }
      originalRequest._retry = true;
      const token = await refreshAccessToken();
      if (token) {
        originalRequest.headers.Authorization = `Bearer ${token}`;
        return api(originalRequest);
      }
    }
    return Promise.reject(error);
  },
);

export default api;
