import { Moon, Sun } from "lucide-react";

import { useTheme } from "@/contexts/ThemeContext";
import { cn } from "@/lib/utils";

export function ThemeToggle() {
  const { theme, setTheme } = useTheme();
  const isDark = theme === "dark";

  return (
    <div className="flex items-center justify-between gap-2 rounded-md px-2 py-1.5">
      <span className="text-xs font-medium text-muted-foreground">Tema</span>
      <div className="flex items-center gap-1 rounded-md border border-border p-0.5">
        <button
          type="button"
          aria-label="Modo claro"
          onClick={() => setTheme("light")}
          className={cn(
            "flex items-center gap-1 rounded px-2 py-1 text-xs transition-colors",
            !isDark ? "bg-primary text-primary-foreground" : "text-muted-foreground hover:text-foreground",
          )}
        >
          <Sun className="h-3.5 w-3.5" />
          Claro
        </button>
        <button
          type="button"
          aria-label="Modo escuro"
          onClick={() => setTheme("dark")}
          className={cn(
            "flex items-center gap-1 rounded px-2 py-1 text-xs transition-colors",
            isDark ? "bg-primary text-primary-foreground" : "text-muted-foreground hover:text-foreground",
          )}
        >
          <Moon className="h-3.5 w-3.5" />
          Escuro
        </button>
      </div>
    </div>
  );
}
