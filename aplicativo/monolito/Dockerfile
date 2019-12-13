# #### CONSTRUCCION MVN PACKAGE (JAR) ####
# FROM maven:3.6-jdk-8-alpine AS builder
# WORKDIR /app

# COPY pom.xml .
# RUN mvn -B -e -C -T 1C org.apache.maven.plugins:maven-dependency-plugin:3.0.2:go-offline

# COPY src ./src
# RUN mvn clean package -Dmaven.test.skip=true

#### EJECUCION DEL JAR DE SPRING BOOT####
FROM openjdk:8-jdk-alpine
LABEL maintainer="w.marchanaranda@gmail.com"

ENV DOCKERIZE_VERSION v0.6.1
#Podemos descargar dockerize o copiarlo desde un directorio
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

#COPY --from=builder /app/target/app*.jar /app.jar
COPY target/app*.jar /app.jar

## ESTA APP USA AWS COGNITO, ENVIAR LOS VALORES DE CLIENT-ID y USER-POOL-ID MEDIANTE VARIABLES DE ENTORNO
ENV userPoolId="123123"
ENV clientId="123123"

ENTRYPOINT exec java -jar /app.jar
EXPOSE 8090