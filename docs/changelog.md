# Changelog

Todas as alterações relevantes do projeto **OrçaZap** são registradas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/)
e o projeto adota o versionamento semântico ([SemVer](https://semver.org/lang/pt-BR/)).

---

## [Não Lançado]

### Adicionado
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
- Definida a **Arquitetura Hexagonal (Ports & Adapters)** para o backend, organizada por
  contexto de domínio (`tenant`, `auth`, `customer`, `budget`) + `shared/`. README e `tasks.md`
  atualizados com o diagrama, a estrutura de pacotes e as tasks refletindo domínio / ports / adapters.

### Planejado (próximas etapas)
- Validação end-to-end do `docker compose up --build`.
- Modelagem das entidades JPA e configuração do banco H2.
- Implementação de multi-tenancy por `tenant_id`.
- Autenticação JWT com Access Token + Refresh Token em cookie HTTP-only.
- APIs de CRM e Orçamentos.
- Front-end React + Vite + shadcn/ui (Dashboard, CRM, Construtor de Orçamentos).
- Integração com WhatsApp via `wa.me`.
- Landing Page de vendas.

---

> **Convenção:** cada entrega de task do [`tasks.md`](tasks.md) deve adicionar uma linha
> correspondente neste changelog, agrupada em **Adicionado / Alterado / Corrigido / Removido**.
