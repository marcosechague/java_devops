FROM maven:3.6.1-jdk-8

LABEL maintainer="w.marchanaranda@gmail.com"

ARG docker_version=docker-ce_18.06.3~ce~3-0~debian_amd64.deb
ARG docker_compose_version=1.23.2
ARG NODE_VERSION=8.9.4
ARG sonar_scanner_version=3.3.0.1492

USER root

RUN apt-get update -yq && apt-get install -q -y \
    && apt-get install apt-utils zip git curl python2.7 python-pip -yq \
    iptables libapparmor1 libdevmapper1.02.1 libltdl7 libseccomp2 xz-utils g++ unzip jq -y \
    && apt-get clean

RUN curl -O https://files-cdn.liferay.com/mirrors/download.oracle.com/otn-pub/java/jdk/7u80-b15/jdk-7u80-linux-x64.tar.gz && \
    tar -xvf jdk-7u80-linux-x64.tar.gz && \
    mkdir -p /usr/lib/jvm/jdk1.7/ && \
    mv jdk1.7*/* /usr/lib/jvm/jdk1.7/ && \
    rm jdk-7u80-linux-x64.tar.gz && \
    ls -al /usr/lib/jvm/ && java -version

RUN curl -O https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_linux-x64_bin.tar.gz && \
    tar -xvf openjdk-11.0.2_linux-x64_bin.tar.gz && \
    mkdir -p /usr/lib/jvm/java-11-openjdk/ && \
    mv jdk-11.0.2*/* /usr/lib/jvm/java-11-openjdk && \
    rm openjdk-11.0.2_linux-x64_bin.tar.gz && \
    ls -al /usr/lib/jvm/ && java -version

RUN git config --system user.name "william.marchan" \
    && git config --system user.email "w.marchanaranda@gmail.com"

COPY settings.xml /usr/share/maven/conf/settings.xml

#### DOCKER IN DOCKER ####
ADD https://download.docker.com/linux/debian/dists/stretch/pool/stable/amd64/$docker_version /opt/
ADD https://nodejs.org/dist/v$NODE_VERSION/node-v$NODE_VERSION-linux-x64.tar.xz /opt/

RUN dpkg -i /opt/$docker_version && \
    curl -L https://github.com/docker/compose/releases/download/$docker_compose_version/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose \
    && mkdir -p /usr/share/node \
    && tar -xf /opt/node-v$NODE_VERSION-linux-x64.tar.xz -C /usr/share/node --strip-components=1 \
    && ln -s /usr/share/node/bin/node /usr/local/bin/node \
    && ln -s /usr/share/node/bin/npm /usr/local/bin/npm \
    && ln -s /usr/share/node/bin/npx /usr/local/bin/npx \
    && chmod +x /usr/local/bin/docker-compose

RUN useradd -u 1001 -d /home/jenkins -m -s /bin/bash jenkins \
    && groupadd -g 992 dockers && usermod -aG dockers jenkins && usermod -aG docker jenkins

#### SONNAR SCANNER ####
ENV SONAR_SCANNER_URL https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-3.2.0.1227-linux.zip
ENV SONAR_SCANNER_HOME /opt/sonar-scanner

# Instalación de SonarQube Scanner
RUN mkdir ${SONAR_SCANNER_HOME} && \
    wget -O sonar-scanner.zip ${SONAR_SCANNER_URL} && \
    unzip sonar-scanner.zip -d ${SONAR_SCANNER_HOME} && \
    rm sonar-scanner.zip

COPY sonar-scanner.properties ${SONAR_SCANNER_HOME}/sonar-scanner-3.2.0.1227-linux/conf/

ENV PATH $PATH:${SONAR_SCANNER_HOME}/sonar-scanner-3.2.0.1227-linux/bin

#### FIN SONNAR SCANNER ####

ENV ARTIFACTORY_CREDENTIALS_USR ""
ENV ARTIFACTORY_CREDENTIALS_PSW ""
ENV ARTIFACTORY_REPOSITORY ""
ENV SONAR_HOST "https://sonarqube:9000"

RUN ls -al /usr/lib/jvm/ && \
    java -version && \
    mvn -v -X && \
    ls -al /usr/local/ && \
    cat /usr/share/maven/conf/settings.xml && \
    which java

WORKDIR /home/jenkins 
RUN chown -R jenkins: /home/jenkins 
VOLUME /home/jenkins 
USER jenkins