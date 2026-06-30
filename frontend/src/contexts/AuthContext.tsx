import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from "react";

import api from "@/lib/api/client";
import { tokenStore } from "@/lib/auth/tokenStore";

type AuthContextValue = {
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
};

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  const logout = useCallback(async () => {
    try {
      await api.post("/auth/logout");
    } finally {
      tokenStore.clear();
      setIsAuthenticated(false);
    }
  }, []);

  useEffect(() => {
    tokenStore.setOnUnauthorized(() => {
      setIsAuthenticated(false);
    });

    api
      .post<{ accessToken: string }>("/auth/refresh")
      .then((response) => {
        tokenStore.set(response.data.accessToken);
        setIsAuthenticated(true);
      })
      .catch(() => {
        tokenStore.clear();
        setIsAuthenticated(false);
      })
      .finally(() => setIsLoading(false));
  }, []);

  const login = useCallback(async (email: string, password: string) => {
    const response = await api.post<{ accessToken: string }>("/auth/login", { email, password });
    tokenStore.set(response.data.accessToken);
    setIsAuthenticated(true);
  }, []);

  const value = useMemo(
    () => ({ isAuthenticated, isLoading, login, logout }),
    [isAuthenticated, isLoading, login, logout],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth deve ser usado dentro de AuthProvider");
  }
  return context;
}
