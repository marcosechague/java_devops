pipeline {
    agent { any }
     tools {
        maven 'mvn', 
        java 'jdk8'
    }
    stages {
        stage('build') {
            steps {
                sh 'mvn -f aplicativo/monolito.pom.xml clean package --Dmaven.test.strue=true'
            }
        }

        stage('test') {
            steps {
                sh 'mvn -f aplicativo/monolito.pom.xml test'
            }
        }
    }
}