# Build app
FROM node:lts-alpine3.22 AS build
WORKDIR /app
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build

# Serve using nginx
FROM nginx:stable-alpine3.21
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
