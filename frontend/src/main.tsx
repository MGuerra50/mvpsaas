import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

import { ThemeProvider } from "@/contexts/ThemeContext";
import { AppRouter } from "@/routes/AppRouter";
import "./index.css";

const initialTheme = localStorage.getItem("orcazap-theme");
if (initialTheme === "dark") {
  document.documentElement.classList.add("dark");
}

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ThemeProvider>
      <AppRouter />
    </ThemeProvider>
  </StrictMode>,
);
