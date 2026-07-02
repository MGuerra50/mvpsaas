# Changelog

Todas as alterações relevantes do projeto **OrçaZap** são registradas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/)
e o projeto adota o versionamento semântico ([SemVer](https://semver.org/lang/pt-BR/)).

---

## [Não Lançado]

### Adicionado
- **Módulo Produtos (HU-06.2)**:
  - Contexto `product` no backend: CRUD + `PATCH /api/products/{id}/status` (`ACTIVE`/`INACTIVE`).
  - `ProductsPage` com data table, busca, paginação, modal de criação/edição e toggle de status.
  - Item **Produtos** na sidebar; catálogo do PDV passa a consumir a API (somente ativos).
  - Seed de produtos em `data.sql` e testes `ProductControllerIntegrationTest`.
- **Dashboard evoluído (HU-05.2)**:
  - `GET /api/dashboard/metrics` enriquecido: `statusSummary`, `averageTicket`, `conversionRate`,
    `topCustomers`, `recentBudgets`.
  - Queries agregadas em `BudgetRepositoryPort` (contagem/soma por status, top clientes, orçamentos recentes).
  - `DashboardKpiGrid`, `BudgetStatusChart` (donut), `TopCustomersChart`, `RecentBudgetsTable`.
  - `MonthlyRevenueChart` migrado para **AreaChart**; utilitário compartilhado `lib/format/currency.ts`.
  - Empty states com CTA "Criar orçamento" e badges de status via Tailwind.
- **Construtor de Orçamentos / PDV (EP-07)**:
  - `BudgetsPage` com layout de duas colunas (busca de produtos, descontos, espelho dinâmico).
  - Catálogo mock de produtos, cálculo de totais em tempo real.
  - Botão "Aprovar e Enviar via WhatsApp" com `wa.me`, mensagem formatada e persistência `APPROVED`.
  - Integração com `POST /api/budgets` e `PATCH /api/budgets/{id}/status`.
- **Landing Page (EP-08)**:
  - `LandingPage` pública em `/` com header, hero, recursos, preços, depoimento e footer escuro.
  - App autenticado movido para `/dashboard`, `/customers`, `/budgets`.
- **Dashboard (EP-05)**:
  - `AppLayout` com sidebar fixa (Dashboard, Clientes, Sair).
  - `DashboardPage` com card de Faturamento Total Bruto e gráfico Recharts (`monthlyRevenue`).
  - Integração com `GET /api/dashboard/metrics`.
- **CRM Clientes (EP-06)**:
  - `CustomersPage` com data table (`@tanstack/react-table`), paginação, busca com debounce.
  - `CustomerFormDialog` para criar/editar clientes via API REST.
  - Integração com `GET/POST/PUT/DELETE /api/customers`.
- **APIs de CRM e Orçamentos (EP-03)**:
  - `CustomerController` — CRUD com paginação, busca (`?search=`) e validação.
  - `BudgetController` — listagem por status, criação `DRAFT`, `PATCH /status`.
  - `DashboardController` — `GET /api/dashboard/metrics` (faturamento bruto + série mensal).
  - `GlobalExceptionHandler`, `PageResult`, `TenantContext.requireCurrentTenantId()`.
  - Campo `createdAt` no modelo `Budget` para métricas temporais.
  - Testes: `CustomerControllerIntegrationTest`, `BudgetControllerIntegrationTest`,
    `DashboardControllerIntegrationTest`.
- **Frontend base (EP-04)**:
  - Projeto Vite + React + TypeScript em `frontend/`.
  - Tailwind CSS v4 (tema light, accent `#F97316`), componentes shadcn/ui base.
  - React Router com rotas públicas/privadas, `AuthContext`, `LoginPage`, axios com refresh automático.
  - `Dockerfile` do frontend atualizado com `ARG VITE_API_BASE_URL`.
- **Autenticação JWT (HU-02.1)**: `JwtTokenProvider`, `JwtAuthenticationFilter`, `AuthService`,
  `AuthController` (`POST /api/auth/login`, `/refresh`, `/logout`), cookie HTTP-only para refresh token,
  `CorsConfig`, BCrypt via `PasswordEncoderConfig`.
- Input ports: `LoginUseCase`, `RefreshTokenUseCase`. Output ports: `TokenGenerationPort`,
  `RefreshTokenValidationPort`. Adapter: `JwtTokenAdapter`.
- Testes de integração: `AuthControllerIntegrationTest`.
- **Adapters JPA/H2** por contexto: `*JpaEntity`, `*JpaRepository`, `*PersistenceMapper`,
  `*PersistenceAdapter` para `tenant`, `auth`, `customer` e `budget`.
- Seed inicial em `data.sql` com dois tenants, usuários, clientes e orçamentos de exemplo.
- **Multi-tenancy**: `TenantContext` (ThreadLocal), `TenantContextFilter`, `JwtTenantExtractor`
  (infra JWT mínima com `jjwt`), `TenantHibernateFilterEnabler` e Hibernate Filter `tenantFilter`.
- `SecurityConfig` stateless temporária (`permitAll`) até o EP-02.
- Testes: `TenantContextTest`, `MultiTenantIsolationTest`, `JwtTenantExtractorTest`.
- Esqueleto **hexagonal por contexto** no backend (`tenant`, `auth`, `customer`, `budget`) com camadas
  `domain`, `application/port/{in,out}`, `application/service` e `adapter/{in/web, out/persistence}`.
- Pacote transversal `shared/` (`tenancy`, `security`, `config`).
- Modelos de domínio puros: `Tenant`, `User`, `Customer`, `Budget` (enums `TenantStatus`, `BudgetStatus`).
- Output ports de persistência: `TenantRepositoryPort`, `UserRepositoryPort`, `CustomerRepositoryPort`,
  `BudgetRepositoryPort`.
- Dependência **H2** no `pom.xml` e configuração de datasource/JPA em `application.properties` e
  `application-docker.properties`.
- Reorganização do repositório para a estrutura de **monorepo**:
  - Projeto Spring Boot existente movido para `backend/`.
  - Criadas as pastas `frontend/`, `docs/` e `infra/`.
- `README.md` raiz com visão geral, arquitetura, modelo de dados e instruções de execução.
- `docs/tasks.md` com o backlog completo no formato Scrum (épicos, histórias e tasks).
- `docs/changelog.md` (este arquivo) para histórico de alterações.
- Scaffolding de infraestrutura Docker:
  - `backend/Dockerfile` (multi-stage Maven + JRE 17) e `backend/.dockerignore`.
  - `frontend/Dockerfile` (template; será efetivado após o scaffold do frontend).
  - `docker-compose.yml` na raiz orquestrando backend (`8080`) e frontend (`5173`).
- `frontend/README.md` e `infra/README.md` com a estrutura prevista.
- `.gitignore` estendido para artefatos de Node/frontend e `backend/target`.

### Alterado
- **PDV (EP-07)**: catálogo mock removido; `ProductSearchPanel` integrado com `GET /api/products?status=ACTIVE`.
- **Dashboard (HU-05.2)**: `DashboardPage` refatorada com layout grid responsivo (KPIs, gráficos, tabela);
  `GrossRevenueCard` substituído por `DashboardKpiGrid`.
- Definida a **Arquitetura Hexagonal (Ports & Adapters)** para o backend, organizada por
  contexto de domínio (`tenant`, `auth`, `customer`, `budget`) + `shared/`. README e `tasks.md`
  atualizados com o diagrama, a estrutura de pacotes e as tasks refletindo domínio / ports / adapters.

### Planejado (próximas etapas)
- Validação end-to-end do `docker compose up --build`.

---

> **Convenção:** cada entrega de task do [`tasks.md`](tasks.md) deve adicionar uma linha
> correspondente neste changelog, agrupada em **Adicionado / Alterado / Corrigido / Removido**.
