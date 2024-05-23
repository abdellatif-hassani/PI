FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build  # This should be adjusted if the build command needs to be specific

FROM nginx:alpine
# Remove existing files in Nginx html directory
RUN rm -rf /usr/share/nginx/html/*
# Copy new files from the subdirectory where Angular outputs them
COPY --from=build /app/dist/web1/browser /usr/share/nginx/html
# Ensure all files are readable
RUN chmod -R 755 /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/nginx.conf
