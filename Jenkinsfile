pipeline {
    
    agent any
    
    tools {
        maven 'Maven 3.9.8'
    }
    
    environment {
        // Uses Pipeline Utility Steps plugin to read information from pom.xml into env variables
        REPOSITORY = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
        IMAGE_NAME = "${REPOSITORY}:${VERSION}"
        // DOCKER_HUB_ACCOUNT_ID = 'yahyaromdhane'
        // DOCKER_HUB_TAG = "${DOCKER_HUB_ACCOUNT_ID}/${IMAGE_NAME}"
        REGISTRY = "localhost:5000"
        TAG = "${REGISTRY}/${IMAGE_NAME}"
    }
    
    stages {
    
        stage ('Clone project') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'gitHubCredentials', url: 'https://github.com/yahyaensi/rest-api-k8s']])
            }
        }
    
        stage ('Launch tests') {
            steps {
                sh 'mvn clean test'
            }
        }
    
        stage ('Build docker image') {
            steps {
                sh 'docker build -t ${IMAGE_NAME} .'
            }
        }
        
        stage ('Push docker image to local registry') {
            steps {
            	sh 'docker tag ${IMAGE_NAME} ${TAG}'
                sh 'docker push ${TAG}'
            }
        }
        
        /** stage ('Push docker image to Docker Hub registry') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerHubCredentials', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUsername')]) {
                    sh 'docker tag ${IMAGE_NAME} ${DOCKER_HUB_TAG}'
                    sh 'docker login -u ${dockerHubUsername} -p ${dockerHubPassword} docker.io'
                    sh 'docker push ${DOCKER_HUB_TAG}'
                }
            }
        }*/
        
        stage ('Deploy in K8S') {
            steps {
                kubeconfig(caCertificate: '', credentialsId: 'kubeConfig', serverUrl: 'https://127.0.0.1:32846') {
               		sh 'kubectl apply -f "kubernetes/1-deployment.yaml"'
               		sh 'kubectl apply -f "kubernetes/3-hpa-resources.yaml"'
               		sh 'kubectl apply -f "kubernetes/4-loadbalancer-service.yaml"'
                }
            }
        }
    }
}