# Image de développement
FROM node:20-alpine AS development

WORKDIR /app

# Installation de pnpm
RUN corepack enable

# Copie des fichiers de dépendances
COPY package.json pnpm-lock.yaml ./

# Installation des dépendances
RUN pnpm install

EXPOSE 5173

# Commande par défaut pour le développement
CMD ["pnpm", "dev", "--host"]

# Étape de construction
FROM node:20-alpine AS builder

WORKDIR /app

# Installation de pnpm
RUN corepack enable

# Copie des fichiers de dépendances
COPY package.json pnpm-lock.yaml ./

# Installation des dépendances
RUN pnpm install --frozen-lockfile

# Copie du reste des fichiers
COPY . .

# Variables d'environnement pour la construction
ARG VITE_API_URL
ENV VITE_API_URL $VITE_API_URL

# Construction de l'application
RUN pnpm run build

# Image de production avec Nginx
FROM nginx:alpine AS production

# Copie des fichiers construits
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]