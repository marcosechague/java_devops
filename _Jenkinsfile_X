#!groovy

pipeline {

    agent any

    environment {
        IP_CENTRAL = '192.168.0.2'
        
        SONAR_HOST = 'http://${IP_CENTRAL}:9000'
        PROY_NAME = 'java-devops-mitocode'
        REPO_NAME = 'monolito'
        BRANCH_NAME = "${GIT_BRANCH}"

        NETWORK_AUX = "aplicativo-mvp_default"
        CONTAINER_NAME = "frontend"
        HOST_APP = "http://${CONTAINER_NAME}"
        APP_HEALTHCHECK = "${HOST_APP}/login"

        ARTIFACTORY_USR = "bot-devops-pe"
        ARTIFACTORY_PSW = "devops@2019"
        ARTIFACTORY_REPOSITORY = "devops-pe-mvn"

        SAUCECONNET_USR = "javadevops"
        SAUCECONNECT_KEY = "eeca871b-437c-4299-b02e-61a5b9d21633"
        SAUCECONNECT_TUNNEL = "javadevops"
    }
    stages {

        stage('Initialize'){
            steps{
                script {

                    def pom = readMavenPom file:'aplicativo/monolito/pom.xml'
                    env.POM_VERSION = pom.version

                    def values = "${JOB_NAME}".split('/')
                    env.REPO_NAME = values[1] == null ? "${REPO_NAME}": values[1]
					
					def now = new Date()
					
					if("${GIT_BRANCH}" == "master" || "${GIT_BRANCH}".take(7) == "release") {
                        env.REPO_URL = "devops-pe-mvn-release/"+values[1]+"/"+pom.version+"-"+now.format("YYYYMMdd-HH_mm")
                    } else {
                        env.REPO_URL = "devops-pe-mvn-snapshot/"+values[1]+"/"+pom.version+"-"+now.format("YYYYMMdd-HH_mm")
                    }
                }
            }

        }
        
        stage('Build'){
            steps{
                script {
                    docker.image('maven:3.6-jdk-8-alpine').inside('-v "/home/.m2:/home/.m2"') {
                        sh "mvn -Dmaven.repo.local=/home/.m2/repository -f ${env.WORKSPACE}/aplicativo/monolito/pom.xml --batch-mode clean package -Dmaven.test.skip=true"
                    }
                }
            }
        }
        
        stage('Unit Test'){
            steps{
                script {
                    docker.image('maven:3.6-jdk-8-alpine').inside('-v "/home/.m2:/home/.m2"') {
                        sh "mvn -Dmaven.repo.local=/home/.m2/repository -f ${env.WORKSPACE}/aplicativo/monolito/pom.xml --batch-mode test"
                    }
                }
            }
        }
        
        stage('Scan Sonar'){
            steps{
                script {
                    docker.image('maven:3.6-jdk-8-alpine').inside('-v "/home/.m2:/home/.m2"') {
                        sh "mvn -Dmaven.repo.local=/home/.m2/repository -f ${env.WORKSPACE}/aplicativo/monolito/pom.xml --batch-mode sonar:sonar -Dsonar.host.url=${SONAR_HOST}"
                    }
                }
            }
        }
        
        stage('E2E Test'){
            steps{
                parallel(
                    test: {
                        script {
                            try{
                                dir('aplicativo/angular'){
                                    docker.image('trion/ng-cli:8.3.14').inside() {
                                        sh "npm install"
                                        sh "ng build"
                                    }
                                }

                                dir('aplicativo'){
                                    echo "### up environment with docker-compose ###"

                                    docker.image('wjma90/mvn3-jdk8-curso-devops').inside('--network="${NETWORK_AUX}" -e "ARTIFACTORY_CREDENTIALS_USR=${ARTIFACTORY_USR}" -e "ARTIFACTORY_CREDENTIALS_PSW=${ARTIFACTORY_PSW}" -e "ARTIFACTORY_REPOSITORY=${ARTIFACTORY_REPOSITORY}" -v "/var/run/docker.sock:/var/run/docker.sock"') {
                                        sh "docker-compose -f docker-compose-all.yml up -d sauceconnect"
                                        sh "docker inspect sauceconnect"
                                        
                                        sh "docker-compose -f docker-compose-all.yml up -d --build"
                                        sh "docker network connect ${NETWORK_AUX} ${CONTAINER_NAME}"
                                        
                                        //waiting for the application
                                        timeout(time: 300, unit: 'SECONDS') {
                                            waitUntil {
                                                try {
                                                    sh "curl -s --head  --request GET  ${APP_HEALTHCHECK} | grep '200'"
                                                    return true
                                                } catch (Exception e) {
                                                        return false
                                                }
                                            }
                                        }

                                        echo "### HUBO CONEXION CON LA APLICACION ###"

                                        sh "docker network connect ${NETWORK_AUX} sauceconnect"

                                        sh "mvn -Dmaven.repo.local=/home/.m2/repository -f ../testing/e2e/pom.xml --batch-mode clean test -P e2e-remote -Dsaucelabs.proxy=${SAUCECONNET_TUNNEL} -Denvironment=REMOTE -Dplatform=ALL -DSAUCE_KEY=${SAUCECONNET_KEY} -DSAUCE_USER=${SAUCECONNECT_USR} -Dbackend=${HOST_APP} -DNAME_PROXY_SAUCECONNECT=sauceconnect"

                                        echo "### Integration TEST FINISHED ###"

                                        sh "docker-compose -f docker-compose-all.yml down -v"
                                    }
                                }
                            }catch(err){
                                echo "Error: ${err}" 
                                try{
                                    dir("aplicativo"){
                                        docker.image('wjma90/mvn3-jdk8-curso-devops').inside('--network="${NETWORK_AUX}" -v "/var/run/docker.sock:/var/run/docker.sock"') {
                                            sh "docker-compose -f docker-compose-all.yml down -v"
                                        }
                                    }
                                }catch(err2){
                                    echo "Error2: ${err2}" 
                                }

                                error "Integration TEST with ERROR... Please verify"
                            }
                        }
                    },
                    logs : {
                        script {
                            //waiting for the application
                            timeout(time: 330, unit: 'SECONDS') {
                                waitUntil {
                                    try {
                                        sh "curl -s --head  --request GET  ${APP_HEALTHCHECK} | grep '200'"
                                        return true
                                    } catch (Exception e) {
                                        return false
                                    }
                                }
                            }

                            dir("aplicativo"){
                                docker.image('wjma90/mvn3-jdk8-curso-devops').inside('--network="${NETWORK_AUX}" -e "ARTIFACTORY_CREDENTIALS_USR=${ARTIFACTORY_USR}" -e "ARTIFACTORY_CREDENTIALS_PSW=${ARTIFACTORY_PSW}" -e "ARTIFACTORY_REPOSITORY=${ARTIFACTORY_REPOSITORY}" -v "/var/run/docker.sock:/var/run/docker.sock"') {
                                    sh "docker-compose -f docker-compose-all.yml ps"
                                    sh "docker-compose -f docker-compose-all.yml logs -f sauceconnect ${CONTAINER_NAME}"
                                }
                            }
                        }
                    }
                )
            }
        }
        /*
        stage('Publish to Artifactory'){
            steps{
                script {
                    docker.image('maven:3.6-jdk-8-alpine').inside() {
                        sh "cat /etc/hosts"
                        sh "mvn -f ${env.WORKSPACE}/aplicativo/monolito/pom.xml --batch-mode sonar:sonar -Dsonar.host.url=${SONAR_HOST}"
                    }
                }
            }
        }

        stage('Deploy to Environment'){
            steps{
                script {
                    docker.image('maven:3.6-jdk-8-alpine').inside() {
                        sh "cat /etc/hosts"
                        sh "mvn -f ${env.WORKSPACE}/aplicativo/monolito/pom.xml --batch-mode sonar:sonar -Dsonar.host.url=${SONAR_HOST}"
                    }
                }
            }
        }
        */
    }
}