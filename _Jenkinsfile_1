#!groovy

pipeline {

    //agent { label 'docker-ec2' } se puede pedir el aprovisionamiento en un entorno de 'docker templates' (jenkins masters)
    agent {
        docker { image 'maven:3.6-jdk-8-alpine' }
    }

    environment {
        TUNNEL_SAUCECONNECT = 'devops'
    }
    stages {

        stage('Build Backend'){
            steps{
                script {
                    sh "mvn -f ${env.WORKSPACE}/aplicativo/monolito/pom.xml -Dmaven.repo.local=/home/.m2/repository --batch-mode package -Dmaven.test.skip=true"
                }
            }
        }
    }
}