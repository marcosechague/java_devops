FROM mysql:5.7.25
LABEL maintainer="w.marchanaranda@gmail.com"

ENV MYSQL_ROOT_PASSWORD=toor
ENV MYSQL_DATABASE=geoserviciosbd
ENV MYSQL_ROOT_HOST=%

COPY scripts-mysql/ /docker-entrypoint-initdb.d/
CMD ["--default-authentication-plugin=mysql_native_password"]