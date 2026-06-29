# OrçaZap — SaaS B2B de Gestão de Clientes e Orçamentos via WhatsApp

> Prova de conceito de alto padrão para um SaaS B2B focado em **gestão de clientes (CRM)** e
> **emissão rápida de orçamentos enviados diretamente pelo WhatsApp**.

Sistema **multi-tenant** (banco de dados compartilhado), com back-end em **Java + Spring Boot**,
front-end em **React + Vite + shadcn/ui** e infraestrutura orquestrada via **Docker**.

Idioma único: **Português do Brasil (pt-BR)**.

---

## 📐 Arquitetura

```
┌─────────────────────────────┐        ┌──────────────────────────────┐
│        FRONTEND              │        │          BACKEND             │
│  React + Vite + shadcn/ui    │  HTTP  │   Java 17 + Spring Boot      │
│  Tailwind CSS + Recharts     │ ─────► │   API RESTful + JWT          │
│  (Landing, Dashboard, CRM,   │  REST  │   Spring Security            │
│   Construtor de Orçamentos)  │ ◄───── │   Multi-tenant (tenant_id)   │
└─────────────────────────────┘        └──────────────┬───────────────┘
                                                       │
                                              ┌────────▼────────┐
                                              │   Banco H2      │
                                              │ (compartilhado) │
                                              └─────────────────┘
```

### Arquitetura Hexagonal (Ports & Adapters)
O back-end segue a **Arquitetura Hexagonal**, isolando o **núcleo de domínio** (regras de negócio
puras, sem dependência de framework) das tecnologias externas (web, persistência, mensageria).
A comunicação acontece exclusivamente por **portas (interfaces)**, implementadas por **adapters**:

```
                      ADAPTERS DE ENTRADA            NÚCLEO (independente de framework)           ADAPTERS DE SAÍDA
                  (driving / inbound adapters)                                              (driven / outbound adapters)
        ┌──────────────────────────────────┐   ┌───────────────────────────────────┐   ┌──────────────────────────────────┐
        │  REST Controllers (Spring Web)   │──►│  Input Ports  →  Application       │──►│  Output Ports  →  Persistência   │
        │  Filtros JWT / Segurança         │   │  (Use Cases / Services)           │   │  (JPA / H2)                      │
        │                                  │   │             Domain Model          │   │                                  │
        └──────────────────────────────────┘   └───────────────────────────────────┘   └──────────────────────────────────┘
```

- **Domain** — entidades de domínio e regras de negócio. **Não** conhece Spring, JPA nem HTTP.
- **Input Ports** (`port/in`) — interfaces de casos de uso (o que a aplicação oferece).
- **Output Ports** (`port/out`) — interfaces de que o domínio precisa (persistir, enviar, etc.).
- **Application** — serviços que implementam os casos de uso e orquestram o domínio.
- **Adapters In** — controllers REST e filtros de segurança (acionam os Input Ports).
- **Adapters Out** — repositórios JPA, entidades de persistência e gateways externos (implementam os Output Ports).

> **Regra da dependência:** as setas sempre apontam **para dentro**. O domínio depende apenas de
> abstrações; os frameworks dependem do domínio — nunca o contrário.

### Multi-Tenancy
Abordagem de **Banco de Dados Compartilhado (Shared Database / Shared Schema)**: todas as tabelas
de negócio carregam a coluna `tenant_id`, que isola logicamente os dados de cada cliente do SaaS.
Tabelas globais (ex.: `Tenant`) não possuem essa coluna.

### Autenticação
Fluxo de emissão de **par de tokens**:
- **Access Token** — JWT de curta duração, enviado no header `Authorization: Bearer`.
- **Refresh Token** — armazenado em **cookie HTTP-only**, usado para renovar o Access Token.

---

## 🗂️ Estrutura do Monorepo

```
mvpsaas/
├── backend/            # API Java + Spring Boot (Maven)
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/           # SPA React + Vite + shadcn/ui
│   └── Dockerfile
├── infra/              # Configurações de infraestrutura/auxiliares
├── docs/
│   ├── changelog.md    # Histórico de todas as alterações
│   └── tasks.md        # Backlog Scrum (épicos, histórias e tasks)
├── docker-compose.yml  # Orquestração local (backend + frontend)
└── README.md
```

### Estrutura de pacotes do backend (Hexagonal)

Organização **por contexto de domínio** (`tenant`, `auth`, `customer`, `budget`), cada um com suas
camadas hexagonais. Pacotes transversais ficam em `shared/`.

```
com.saas.b2b
├── customer/                          # exemplo de contexto (mesma forma para budget, auth, tenant)
│   ├── domain/
│   │   └── model/                     # Customer (domínio puro)
│   ├── application/
│   │   ├── port/
│   │   │   ├── in/                    # ex.: ManageCustomerUseCase (input port)
│   │   │   └── out/                   # ex.: CustomerRepositoryPort (output port)
│   │   └── service/                   # ex.: CustomerService (implementa o use case)
│   └── adapter/
│       ├── in/web/                    # CustomerController + DTOs (driving adapter)
│       └── out/persistence/           # CustomerJpaEntity, CustomerJpaRepository,
│                                      #   CustomerPersistenceAdapter (driven adapter)
│
├── budget/                            # orçamentos (envio ao WhatsApp é feito no frontend)
├── auth/                              # JWT, refresh token, login (input/output ports)
├── tenant/                            # contexto global (sem tenant_id)
│
└── shared/
    ├── tenancy/                       # TenantContext, filtro multi-tenant
    ├── security/                      # configuração Spring Security, filtros JWT
    └── config/                        # configurações transversais
```

---

## 🧱 Modelo de Dados

| Tabela     | Colunas                                                            | Observações              |
|------------|-------------------------------------------------------------------|--------------------------|
| `Tenant`   | `id`, `name`, `status`                                            | Tabela **global**        |
| `User`     | `id`, `tenant_id`, `name`, `email`, `password_hash`              | Senha sempre com hash    |
| `Customer` | `id`, `tenant_id`, `name`, `phone`, `document_id`               | CRM                      |
| `Budget`   | `id`, `tenant_id`, `customer_id`, `total_amount`, `status`      | `DRAFT / APPROVED / REJECTED` |

---

## 🖥️ Front-end — Módulos

A interface segue o layout clássico de SaaS: **Sidebar fixa à esquerda** + área de conteúdo.
São **3 módulos centrais**:

1. **Dashboard Principal** — métricas de topo (Faturamento Total Bruto) e gráficos com **Recharts**.
2. **CRM (Clientes)** — Data Table (shadcn/ui) com paginação na base e busca em tempo real.
3. **Construtor de Orçamentos (PDV)** — layout de duas colunas simétricas:
   - **Esquerda:** busca de produtos e aplicação de descontos.
   - **Direita:** espelho dinâmico do orçamento finalizado.

### Integração com WhatsApp
O botão **"Aprovar e Enviar Instantaneamente via WhatsApp"** formata uma mensagem amigável
com os dados do pedido e abre a URL nativa `wa.me/<numero>?text=<mensagem>`, permitindo o envio
direto pelo aparelho ou WhatsApp Web do vendedor — **sem custos e sem sessões complexas**.

### Landing Page
Funil público de alta conversão (pt-BR): Header com CTA laranja `#F97316`, dobra principal,
recursos, tabela de preços (Básico / **Pro** / Enterprise), prova social e footer escuro.

---

## 🛠️ Stack Técnica

| Camada        | Tecnologias                                                        |
|---------------|-------------------------------------------------------------------|
| Back-end      | Java 17, Spring Boot, Spring Security, Spring Data JPA, JWT       |
| Banco         | H2 (compartilhado / multi-tenant)                                 |
| Front-end     | React, Vite, TypeScript, Tailwind CSS, shadcn/ui, Recharts       |
| Infra         | Docker, Docker Compose                                            |

---

## 🚀 Como Executar (Docker)

> ⚠️ A implementação dos módulos será feita nas próximas etapas (ver [`docs/tasks.md`](docs/tasks.md)).
> Os comandos abaixo refletem o fluxo de execução previsto para o ambiente containerizado.

```bash
# Subir backend + frontend
docker compose up --build

# Backend:  http://localhost:8080
# Frontend: http://localhost:5173
# Console H2: http://localhost:8080/h2-console
```

### Execução local (sem Docker)

```bash
# Backend
cd backend && ./mvnw spring-boot:run

# Frontend
cd frontend && npm install && npm run dev
```

---

## 📚 Documentação

- [`docs/tasks.md`](docs/tasks.md) — Backlog completo (épicos → histórias → tasks) seguindo Scrum.
- [`docs/changelog.md`](docs/changelog.md) — Registro cronológico de todas as alterações.

---

## 🎨 Design System

- Modo de cor **fixo** (definido no início do desenvolvimento — ver `tasks.md`).
- Componentes acessíveis e modulares via **shadcn/ui**.
- Cor de destaque/CTA: **laranja `#F97316`**.
