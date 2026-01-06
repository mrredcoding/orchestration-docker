FROM node:18 AS build

WORKDIR /app

ARG API_BASE_URL
ENV API_BASE_URL=${API_BASE_URL}

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

FROM nginx:alpine

COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]