#!groovy

pipeline {

     agent {
        docker { image 'maven:3.6-jdk-8-alpine' }
    }

    environment {
        SONAR_HOST = 'http://172.18.0.1:9000'
    }
    stages {

        
        stage('Build Backend'){
            steps{
                script {
                    sh "mvn -f ${env.WORKSPACE}/aplicativo/monolito/pom.xml -Dmaven.repo.local=/home/.m2/repository --batch-mode package -Dmaven.test.skip=true"
                }
            }
        }

         stage('Unit Test'){
            steps{
                script {
                    sh "mvn -Dmaven.repo.local=/home/.m2/repository -f ${env.WORKSPACE}/aplicativo/monolito/pom.xml -Dmaven.repo.local=/home/.m2/repository --batch-mode test"
                }
            }
        }
        

        stage('Scan Sonar Backend'){
            steps{
                script {
                    sh "mvn -f ${env.WORKSPACE}/aplicativo/monolito/pom.xml -Dmaven.repo.local=/home/.m2/repository --batch-mode sonar:sonar -Dsonar.host.url=${SONAR_HOST}"
                }
            }
        }
        
    }
}