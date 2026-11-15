# Estágio 1: Compilação do Angular
FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build -- --configuration=production

# Estágio 2: Servidor Web Nginx
FROM nginx:alpine

# Copia a configuração customizada de rotas SPA
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Limpa a pasta padrão do Nginx e copia os arquivos compilados do Angular
# Nota: Se o nome do seu projeto no angular.json não for "front-end", ajuste o caminho abaixo
RUN rm -rf /usr/share/nginx/html/*
COPY --from=build /app/dist/front-end/browser /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
