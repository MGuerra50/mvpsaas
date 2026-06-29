# Infra

Configurações auxiliares de infraestrutura (variáveis de ambiente, scripts e ajustes de deploy).

A orquestração principal fica no [`../docker-compose.yml`](../docker-compose.yml) na raiz do monorepo:

- **backend** — API Spring Boot (porta `8080`).
- **frontend** — SPA React/Vite servida estaticamente (porta `5173`).

O banco **H2** é embarcado no backend, portanto não há container de banco dedicado.
