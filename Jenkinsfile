#!groovy

pipeline {
//    agent {
//        docker {
//            image 'maven:3.5.4-jdk-8'
//            args '--network ci --mount type=volume,source=ci-maven-home,target=/root/.m2'
//        }
//    }

	agent any 
	
	tools { 
        maven 'Maven 3.5.4' 
        jdk 'jdk8' 
    }
	
    environment {
        ORG_NAME = "oscuroweb"
        APP_NAME = "income-predictor-h2o-service"
        APP_CONTEXT_ROOT = "oscuroweb"
        TEST_CONTAINER_NAME = "ci-${APP_NAME}-${BUILD_NUMBER}"
        LOCAL_PATH = "/Users/oscuro/workspace/commitconf2018/income-predictor-data/"
    }

    stages {
        stage('Compile') {
            steps {
                echo "-=- compiling project -=-"
                sh "mvn clean compile"
            }
        }

        stage('Unit tests') {
            steps {
                echo "-=- execute unit tests -=-"
                sh "mvn test"
            }
        }

        stage('Package') {
            steps {
                echo "-=- packaging project -=-"
                sh "mvn package -DskipTests"
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Build Docker image') {
            steps {
                echo "-=- build Docker image -=-"
                script {
                    def image = docker.build("${APP_NAME}:${env.BUILD_ID}")
                }
            }
        }

        stage('Run Docker image') {
            steps {
                echo "-=- run Docker image -=-"    
                sh "docker rm -f ${env.TEST_CONTAINER_NAME}"
                sh "docker run -p 8082:8082 --network-alias=[income-predictor-service] -v ${env.LOCAL_PATH}:/data/income-predictor -e LOCAL_PATH=/data/income-predictor --name ${env.TEST_CONTAINER_NAME} -d ${APP_NAME}:${env.BUILD_ID}"
            }
        }

        stage('Integration tests') {
            steps {
                echo "-=- execute integration tests -=-"
                echo "Not an executable project so no integration test phase needed"
            }
        }

        stage('Performance tests') {
            steps {
                echo "-=- execute performance tests -=-"
                echo "Not an executable project so no performance test phase needed"
            }
        }

        stage('Dependency vulnerability tests') {
            steps {
                echo "-=- run dependency vulnerability tests -=-"
                sh "mvn dependency-check:check"
            }
        }

        stage('Push Artifact') {
            steps {
                echo "-=- push Artifact -=-"
                script {
                	docker.withRegistry('https://hub.docker.com/u/oscurorestalion/', 'docker-hub') {
        				image.push()
        			}    
            	}
            }
        }
    }

    post {
        always {
            echo "-=- remove deployment -=-"
            echo "Not an executable project so no Docker image needed"
        }
    }
}