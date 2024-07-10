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
        DOCKER_HUB_ACCOUNT_ID = 'yahyaromdhane'
        TAG = "${DOCKER_HUB_ACCOUNT_ID}/${IMAGE_NAME}"
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
        
        stage ('Push docker image to registry') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerHubCredentials', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUsername')]) {
                    sh 'docker tag ${IMAGE_NAME} ${TAG}'
                    sh 'docker login -u ${dockerHubUsername} -p ${dockerHubPassword} docker.io'
                    sh 'docker push ${TAG}'
                }
            }
        }
        
        stage ('Deploy in K8S') {
            steps {
                kubeconfig(caCertificate: '', credentialsId: 'kubeConfig', serverUrl: 'https://127.0.0.1:32771') {
               		sh '/usr/local/bin/minikube image load ${IMAGE_NAME}'
               		sh 'kubectl apply -f "kubernetes/1-deployment.yaml"'
               		sh 'kubectl apply -f "kubernetes/3-hpa-resources.yaml"'
               		sh 'kubectl apply -f "kubernetes/4-loadbalancer-service.yaml"'
                }
            }
        }
    }
}