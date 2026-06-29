# Backlog do Projeto — OrçaZap (SaaS B2B)

Planejamento de execução seguindo o framework **Scrum**, organizado em
**Épicos → Histórias de Usuário → Tasks técnicas**.

> **Legenda de status:** ⬜ A fazer · 🟨 Em andamento · ✅ Concluído
>
> **Estimativa:** Story Points (SP) no padrão Fibonacci (1, 2, 3, 5, 8, 13).

---

## 🎯 Decisões de Projeto (Sprint 0 — Definições)

| Decisão                | Escolha                                            |
|------------------------|----------------------------------------------------|
| Idioma                 | Português do Brasil (pt-BR), único                 |
| Modo de cor            | **Light Mode** (fixo)                              |
| Cor de destaque/CTA    | Laranja `#F97316`                                 |
| Arquitetura backend    | **Hexagonal (Ports & Adapters)**, por contexto de domínio |
| Multi-tenancy          | Banco de Dados Compartilhado (`tenant_id`)        |
| Banco de dados         | H2                                                 |
| Java                   | 17                                                 |

---

## 🗺️ Roadmap de Sprints

| Sprint   | Foco                                              | Épicos             |
|----------|---------------------------------------------------|--------------------|
| Sprint 0 | Fundação do monorepo e infraestrutura             | EP-00              |
| Sprint 1 | Backend: dados, multi-tenancy e autenticação      | EP-01, EP-02       |
| Sprint 2 | Backend: APIs de CRM e Orçamentos                 | EP-03              |
| Sprint 3 | Frontend: base, layout e Dashboard                | EP-04, EP-05       |
| Sprint 4 | Frontend: CRM e Construtor de Orçamentos + WhatsApp | EP-06, EP-07     |
| Sprint 5 | Landing Page e polimento final                    | EP-08              |

---

## 📦 EP-00 — Fundação do Monorepo e Infraestrutura

> Estruturar o repositório, ferramentas e ambiente Docker.

### 🧩 HU-00.1 — Como dev, quero o monorepo organizado para separar backend e frontend.  `3 SP`
- ✅ TASK-00.1.1 — Reorganizar projeto Spring Boot existente para a pasta `backend/`.
- ✅ TASK-00.1.2 — Criar pastas `frontend/`, `docs/` e `infra/`.
- ✅ TASK-00.1.3 — Criar `README.md` raiz com visão geral, arquitetura e instruções.
- ✅ TASK-00.1.4 — Criar `docs/changelog.md` e `docs/tasks.md`.

### 🧩 HU-00.2 — Como dev, quero rodar o sistema via Docker.  `5 SP`  🟨
- ✅ TASK-00.2.1 — Criar `backend/Dockerfile` (build multi-stage Maven + JRE).
- ✅ TASK-00.2.2 — Criar `frontend/Dockerfile` (build Vite + serve) — *template; depende do scaffold do EP-04*.
- ✅ TASK-00.2.3 — Criar `docker-compose.yml` orquestrando backend + frontend.
- ⬜ TASK-00.2.4 — Validar `docker compose up --build` end-to-end (após scaffolds de backend/frontend).

---

## 📦 EP-01 — Modelagem de Dados e Multi-Tenancy

> Persistência relacional estrita com isolamento por `tenant_id`.

### 🧩 HU-01.0 — Como dev, quero o esqueleto hexagonal por contexto.  `3 SP`
- ⬜ TASK-01.0.1 — Criar pacotes por contexto (`tenant`, `auth`, `customer`, `budget`) com camadas `domain`, `application/port/{in,out}`, `application/service`, `adapter/{in/web, out/persistence}`.
- ⬜ TASK-01.0.2 — Criar pacote `shared/` (`tenancy`, `security`, `config`).
- ⬜ TASK-01.0.3 — (Opcional) Teste ArchUnit garantindo a regra de dependência (domínio não importa Spring/JPA).

### 🧩 HU-01.1 — Como sistema, quero o domínio modelado conforme o negócio.  `5 SP`
- ⬜ TASK-01.1.1 — Adicionar dependência H2 ao `pom.xml` e configurar `application.properties`.
- ⬜ TASK-01.1.2 — Modelo de domínio `Tenant` (`id`, `name`, `status`) — contexto global.
- ⬜ TASK-01.1.3 — Modelo de domínio `User` (`id`, `tenant_id`, `name`, `email`, `password_hash`).
- ⬜ TASK-01.1.4 — Modelo de domínio `Customer` (`id`, `tenant_id`, `name`, `phone`, `document_id`).
- ⬜ TASK-01.1.5 — Modelo de domínio `Budget` (`id`, `tenant_id`, `customer_id`, `total_amount`, `status` enum).
- ⬜ TASK-01.1.6 — Definir **output ports** de persistência (`...RepositoryPort`) por contexto.

### 🧩 HU-01.3 — Como sistema, quero adapters de persistência (JPA/H2).  `5 SP`
- ⬜ TASK-01.3.1 — `...JpaEntity` (mapeamento JPA) separada do modelo de domínio.
- ⬜ TASK-01.3.2 — `...JpaRepository` (Spring Data) por contexto.
- ⬜ TASK-01.3.3 — `...PersistenceAdapter` implementando os output ports (mapeia domínio ↔ entidade JPA).
- ⬜ TASK-01.3.4 — Seed de dados (`data.sql`) com tenant, usuário e clientes de exemplo.

### 🧩 HU-01.2 — Como sistema, quero isolar dados por tenant automaticamente.  `8 SP`
- ⬜ TASK-01.2.1 — `TenantContext` (ThreadLocal) para o tenant da requisição.
- ⬜ TASK-01.2.2 — Filtro/interceptor que resolve o `tenant_id` a partir do JWT.
- ⬜ TASK-01.2.3 — Estratégia de filtro automático por `tenant_id` nas queries (Hibernate Filter).
- ⬜ TASK-01.2.4 — Testes garantindo que um tenant não acessa dados de outro.

---

## 📦 EP-02 — Autenticação e Segurança (JWT)

> Login seguro com par de tokens e cookie HTTP-only.
> No contexto `auth`: casos de uso como **input ports**, geração de token como **output port**;
> controllers e filtros JWT são **adapters de entrada**.

### 🧩 HU-02.1 — Como usuário, quero me autenticar e receber tokens.  `8 SP`
- ⬜ TASK-02.1.1 — Configurar Spring Security (stateless, filtros, CORS).
- ⬜ TASK-02.1.2 — Serviço de geração/validação de JWT (Access Token curto).
- ⬜ TASK-02.1.3 — Geração de Refresh Token e gravação em **cookie HTTP-only**.
- ⬜ TASK-02.1.4 — Endpoint `POST /api/auth/login`.
- ⬜ TASK-02.1.5 — Endpoint `POST /api/auth/refresh` (lê cookie, emite novo Access Token).
- ⬜ TASK-02.1.6 — Endpoint `POST /api/auth/logout` (limpa cookie).
- ⬜ TASK-02.1.7 — Hash de senha com BCrypt.

---

## 📦 EP-03 — APIs de Negócio (CRM e Orçamentos)

> Endpoints RESTful para clientes e orçamentos, sempre tenant-aware.
> Cada operação = um **input port** (use case) implementado por um **service** no núcleo; os
> controllers REST são **adapters de entrada**. O envio ao WhatsApp ocorre no **frontend** (abre
> `wa.me`); ao backend cabe apenas persistir o orçamento como `APPROVED`.

### 🧩 HU-03.1 — Como vendedor, quero gerenciar clientes via API.  `5 SP`
- ⬜ TASK-03.1.1 — `GET /api/customers` com paginação e busca (`?search=`).
- ⬜ TASK-03.1.2 — `POST /api/customers`.
- ⬜ TASK-03.1.3 — `GET/PUT/DELETE /api/customers/{id}`.
- ⬜ TASK-03.1.4 — DTOs + validação (`@Valid`) + tratamento de erros padronizado.

### 🧩 HU-03.2 — Como vendedor, quero gerenciar orçamentos via API.  `5 SP`
- ⬜ TASK-03.2.1 — `GET /api/budgets` (lista com status).
- ⬜ TASK-03.2.2 — `POST /api/budgets` (cria como `DRAFT`).
- ⬜ TASK-03.2.3 — `PATCH /api/budgets/{id}/status` (`APPROVED`/`REJECTED`).
- ⬜ TASK-03.2.4 — `GET /api/dashboard/metrics` (Faturamento Total Bruto + séries para gráficos).

---

## 📦 EP-04 — Fundação do Front-end

> Projeto React + Vite + Tailwind + shadcn/ui pronto para os módulos.

### 🧩 HU-04.1 — Como dev, quero o scaffold do frontend configurado.  `5 SP`
- ⬜ TASK-04.1.1 — Iniciar projeto Vite + React + TypeScript.
- ⬜ TASK-04.1.2 — Configurar Tailwind CSS e tema fixo (Light Mode, paleta + `#F97316`).
- ⬜ TASK-04.1.3 — Inicializar shadcn/ui e instalar componentes base (button, table, dialog, form, input).
- ⬜ TASK-04.1.4 — Configurar React Router (rotas públicas x privadas).
- ⬜ TASK-04.1.5 — Cliente HTTP (axios/fetch) com interceptor de Access Token e refresh automático.

### 🧩 HU-04.2 — Como usuário, quero autenticar pela interface.  `3 SP`
- ⬜ TASK-04.2.1 — Tela de Login.
- ⬜ TASK-04.2.2 — Guarda de rota e contexto de autenticação.

---

## 📦 EP-05 — Módulo Dashboard

### 🧩 HU-05.1 — Como gestor, quero ver métricas e gráficos.  `5 SP`
- ⬜ TASK-05.1.1 — Layout SaaS com **Sidebar fixa** e área de conteúdo.
- ⬜ TASK-05.1.2 — Cards de métricas de topo (Faturamento Total Bruto).
- ⬜ TASK-05.1.3 — Gráficos centrais com **Recharts**.

---

## 📦 EP-06 — Módulo CRM (Clientes)

### 🧩 HU-06.1 — Como vendedor, quero listar e buscar clientes.  `5 SP`
- ⬜ TASK-06.1.1 — **Data Table** (shadcn/ui) com colunas de cliente.
- ⬜ TASK-06.1.2 — Paginação na base da tabela.
- ⬜ TASK-06.1.3 — Busca em tempo real.
- ⬜ TASK-06.1.4 — Modal de criação/edição de cliente.

---

## 📦 EP-07 — Construtor de Orçamentos (PDV) + WhatsApp

### 🧩 HU-07.1 — Como vendedor, quero montar um orçamento em duas colunas.  `8 SP`
- ⬜ TASK-07.1.1 — Layout de duas colunas simétricas.
- ⬜ TASK-07.1.2 — Coluna esquerda: busca de produtos e aplicação de descontos.
- ⬜ TASK-07.1.3 — Coluna direita: espelho dinâmico do orçamento (totais em tempo real).

### 🧩 HU-07.2 — Como vendedor, quero aprovar e enviar pelo WhatsApp.  `3 SP`
- ⬜ TASK-07.2.1 — Botão "Aprovar e Enviar Instantaneamente via WhatsApp".
- ⬜ TASK-07.2.2 — Formatar mensagem amigável com os dados do pedido.
- ⬜ TASK-07.2.3 — Abrir `wa.me/<numero>?text=<mensagem>` e persistir orçamento como `APPROVED`.

---

## 📦 EP-08 — Landing Page de Venda

### 🧩 HU-08.1 — Como visitante, quero uma página de vendas de alta conversão.  `8 SP`
- ⬜ TASK-08.1.1 — Header: logotipo + botão laranja `#F97316` "Começar Grátis".
- ⬜ TASK-08.1.2 — Dobra principal: headline + imagem 3D do dashboard flutuante.
- ⬜ TASK-08.1.3 — Recursos: cards "Arquitetura Multi-Tenant Segura" e "Checkout Integrado Pix e Cartão".
- ⬜ TASK-08.1.4 — Preços: tabela com Básico / **Pro (destaque)** / Enterprise.
- ⬜ TASK-08.1.5 — Prova social: depoimento de Mônica Pereira (resposta < 3 min, ↑ faturamento).
- ⬜ TASK-08.1.6 — Footer escuro com copyright.

---

## ✅ Definition of Done (DoD)

Uma task é considerada concluída quando:
1. O código compila e roda sem erros.
2. Segue o padrão multi-tenant (quando aplicável).
3. Está em pt-BR e no Light Mode definido.
4. Foi registrada no [`changelog.md`](changelog.md).
5. Não quebra build do `docker compose up`.
