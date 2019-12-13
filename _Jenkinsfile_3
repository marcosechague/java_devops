#!groovy

pipeline {

    agent any

    environment {
        SONAR_HOST = 'http://192.168.43.121:9000'

        PROY_NAME = 'java-devops-mitocode'
        REPO_NAME = 'monolito'
        BRANCH_NAME = "${GIT_BRANCH}"

        NETWORK_AUX = "aplicativo-mvp_default"
        
        CONTAINER_NAME = "monolito"
        HOST_APP = "http://${CONTAINER_NAME}:8090"
        APP_HEALTHCHECK = "${HOST_APP}/v2/api-docs"

        ARTIFACTORY_USR = "bot-devops-pe"
        ARTIFACTORY_PSW = "devops@2019"
        ARTIFACTORY_REPOSITORY = "devops-pe-mvn"
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

        stage('Integration Test'){
            steps{
                parallel(
                    test: {
                        script {
                            try{
                                echo "### creating environment with docker-compose ###"
                                dir("aplicativo"){
                                    docker.image('wjma90/mvn3-jdk8-curso-devops').inside('--network="${NETWORK_AUX}" -e "ARTIFACTORY_CREDENTIALS_USR=${ARTIFACTORY_USR}" -e "ARTIFACTORY_CREDENTIALS_PSW=${ARTIFACTORY_PSW}" -e "ARTIFACTORY_REPOSITORY=${ARTIFACTORY_REPOSITORY}" -v "/var/run/docker.sock:/var/run/docker.sock"') {
                                        sh "docker-compose up -d --build"
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

                                        sh "mvn -Dmaven.repo.local=/home/.m2/repository -f ../testing/integration/pom.xml --batch-mode test -Dbackend=${HOST_APP}"

                                        echo "### Integration TEST FINISHED ###"

                                        sh "docker-compose down -v"
                                    }
                                }
                            }catch(err){
                                echo "Error: ${err}" 
                                try{
                                    dir("aplicativo"){
                                        docker.image('wjma90/mvn3-jdk8-curso-devops').inside('--network="${NETWORK_AUX}" -v "/var/run/docker.sock:/var/run/docker.sock"') {
                                            sh "docker-compose down -v"
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
                                    sh "docker-compose ps"
                                    sh "docker-compose logs -f ${CONTAINER_NAME} mysql_server"
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}