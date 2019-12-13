#!groovy

pipeline {

    agent any

    environment {
        SONAR_HOST = 'http://192.168.43.121:9000'
        PROY_NAME = 'java-devops-mitocode'
        REPO_NAME = 'frontend'
        BRANCH_NAME = "${GIT_BRANCH}"
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
        
        stage('Build Backend'){
            steps{
                script {
                    docker.image('maven:3.6-jdk-8-alpine').inside() {
                        sh "mvn -f ${env.WORKSPACE}/aplicativo/monolito/pom.xml -Dmaven.repo.local=/home/.m2/repository --batch-mode package -Dmaven.test.skip=true"
                    }
                }
            }
        }
        
        stage('Build Frontend'){
            steps{
                script {
                    dir('aplicativo/angular'){
                        docker.image('trion/ng-cli:8.3.14').inside() {
                            sh "npm install"
                            sh "ng build --prod"
                        }
                    }
                }
            }
        }
        
        stage('Scan Sonar Backend'){
            steps{
                script {
                    docker.image('maven:3.6-jdk-8-alpine').inside() {
                        sh "mvn -f ${env.WORKSPACE}/aplicativo/monolito/pom.xml -Dmaven.repo.local=/home/.m2/repository --batch-mode sonar:sonar -Dsonar.host.url=${SONAR_HOST}"
                    }
                }
            }
        }
        
        stage('Scan Sonar Frontend'){
            steps{
                script {
                    dir('aplicativo/angular'){
                        docker.image('wjma90/mvn3-jdk8-curso-devops').inside() {
                            sh "sonar-scanner -Dsonar.host.url=${SONAR_HOST} -Dsonar.projectKey='${env.PROY_NAME}:${env.REPO_NAME}:${env.BRANCH_NAME}' -Dsonar.projectName='${env.REPO_NAME} ${env.BRANCH_NAME}'"
                        }
                    }
                }
            }
        }
    }
}