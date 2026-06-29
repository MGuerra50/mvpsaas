# ---------- Estágio de build ----------
# NOTA: este Dockerfile só fará build após o scaffold do frontend (ver docs/tasks.md, EP-04).
FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build

# ---------- Estágio de runtime ----------
FROM node:20-alpine AS runtime
WORKDIR /app

RUN npm install -g serve
COPY --from=build /app/dist ./dist

EXPOSE 5173
CMD ["serve", "-s", "dist", "-l", "5173"]
