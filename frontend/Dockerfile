FROM nginx:alpine
COPY index.html /usr/share/nginx/html/index.html
RUN sed -i '1i\
location /api { proxy_pass http://backend:8080; }' /etc/nginx/conf.d/default.conf
