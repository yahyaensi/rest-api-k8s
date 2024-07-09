pipeline {
    
    agent any
    
    tools {
        maven 'Maven 3.9.8'
    }
    
    environment {
        // Uses Pipeline Utility Steps plugin to read information from pom.xml into env variables
        IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
        DOCKER_HUB_ACCOUNT_ID = 'yahyaromdhane'
        TAG = "${DOCKER_HUB_ACCOUNT_ID}/${IMAGE}:${VERSION}"
    }
    
    stages {
        stage('Launch tests') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'gitHubCredentials', url: 'https://github.com/yahyaensi/rest-api-k8s']])
                sh 'mvn clean test'
            }
        }
        stage('Build docker image') {
            steps {
                sh 'docker build -t ${TAG} .'
            }
        }
        stage('Push docker image to registry') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerHubCredentials', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUsername')]) {
                    sh 'docker login -u ${dockerHubUsername} -p ${dockerHubPassword} docker.io'
                    sh 'docker push ${TAG}'
                }
            }
        }
    }
}