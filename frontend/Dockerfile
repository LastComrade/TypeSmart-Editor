# For NPM
FROM node:18-alpine as build

ARG REACT_APP_API_BASE_URL
ENV REACT_APP_API_BASE_URL=$REACT_APP_API_BASE_URL

ARG REACT_APP_ENV

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build:$REACT_APP_ENV

# For running the server with the static HTML files
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
