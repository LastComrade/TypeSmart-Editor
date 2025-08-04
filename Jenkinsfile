pipeline {
    agent {
        docker {
            image 'konarklohat2611/typesmart-jenkins:latest'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        PROJECT_NAME = 'typesmart-editor'
        BRANCH_NAME = "${env.GIT_BRANCH ? env.GIT_BRANCH.replaceFirst(/^origin\//, '') : 'dev'}"
    }

    stages {
        stage('Clone') {
            steps {
                echo "Cloning branch: ${env.BRANCH_NAME}"
                checkout scm
            }
        }

        stage('Run Unit Tests') {
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

                    if (!fileExists(composeFile)) {
                        error "No compose file found for branch: ${branch}"
                    }

                    echo "Building and deploying using: ${composeFile}"

                    sh """
                        docker compose -f docker-compose.yml -f ${composeFile} down || true
                        docker compose -f docker-compose.yml -f ${composeFile} build
                        docker compose -f docker-compose.yml -f ${composeFile} up -d
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
