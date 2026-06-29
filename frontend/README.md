# Frontend — OrçaZap

SPA em **React + Vite + TypeScript**, estilizada com **Tailwind CSS** e **shadcn/ui**.

> 🚧 **Placeholder.** O scaffold do projeto Vite ainda será gerado na **Sprint 3 (EP-04)**.
> Ver [`../docs/tasks.md`](../docs/tasks.md).

## Estrutura prevista

```
frontend/
├── src/
│   ├── components/      # Componentes shadcn/ui e compartilhados
│   ├── layouts/         # Layout SaaS (Sidebar fixa + conteúdo)
│   ├── pages/
│   │   ├── landing/     # Landing Page pública
│   │   ├── dashboard/   # Dashboard + Recharts
│   │   ├── crm/         # CRM (Data Table)
│   │   └── budgets/     # Construtor de Orçamentos (PDV) + WhatsApp
│   ├── lib/             # Cliente HTTP, utils, formatadores
│   └── routes/          # Rotas públicas x privadas
├── index.html
├── package.json
├── vite.config.ts
└── Dockerfile
```

## Convenções
- Idioma: **pt-BR** único.
- Modo de cor: **Light Mode** fixo.
- Cor de destaque/CTA: laranja `#F97316`.
