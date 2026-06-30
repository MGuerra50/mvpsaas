let accessToken: string | null = null;
let onUnauthorized: (() => void) | null = null;

export const tokenStore = {
  get(): string | null {
    return accessToken;
  },
  set(token: string | null) {
    accessToken = token;
  },
  clear() {
    accessToken = null;
  },
  setOnUnauthorized(callback: () => void) {
    onUnauthorized = callback;
  },
  notifyUnauthorized() {
    onUnauthorized?.();
  },
};
