pipeline {
    agent {
        docker {
            image 'maven:3.9.6-eclipse-temurin-17'
            args '-v /root/.m2:/root/.m2'
        }
    }

    environment {
        PROJECT_NAME = 'typesmart-editor'
        BRANCH_NAME = "${env.GIT_BRANCH ? env.GIT_BRANCH.replaceFirst(/^origin\//, '') : 'dev'}"
    }

    stages {
        stage('clone') {
            steps {
                echo "Cloning branch: ${env.BRANCH_NAME}"
                checkout scm
            }
        }

        stage('Run Unit tests') {
            steps {
                dir('suggestion-service') {
                    echo "Running unit tests for suggestion-service"
                    sh 'mvn test'
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build and Deploy') {
            steps {
                script {
                    def branch = env.BRANCH_NAME
                    def composeFile = "docker-compose.${branch}.yml"
                    def exists = fileExists(composeFile)

                    if (!exists) {
                        error "No compose file found for branch: ${env.BRANCH_NAME}"
                    }

                    echo "Building for environment: ${env.BRANCH_NAME}"

                    sh """
                        docker-compose -f docker-compose.yml -f ${composeFile} down || true
                        docker-compose -f docker-compose.yml -f ${composeFile} build
                        docker-compose -f docker-compose.yml -f ${composeFile} up -d
                    """
                }
            }
        }
    }

    post {
        success {
            echo "CI/CD pipeline completed successfully for branch: ${env.BRANCH_NAME}"
        }

        failure {
            echo "CI/CD pipeline failed for branch: ${env.BRANCH_NAME}"
        }
    }
}