FROM node:20-alpine AS build

WORKDIR /app
COPY package.json package-lock.json* ./
COPY .env ./
RUN npm install
COPY public public
COPY src src
COPY tsconfig.json ./
RUN npm run build

FROM nginx:1.25-alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
