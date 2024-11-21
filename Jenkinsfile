pipeline {
    agent any
    environment {
        API_ACCESS_KEY = credentials('ncp-api-access-key')
        API_SECRET_KEY = credentials('ncp-api-secret-key')
        JASYPT_KEY = credentials('JASYPT_KEY')
    }

    stages {
        stage('Prepare') {
            steps {
                echo 'Preparing...'
                git branch: 'main', url: 'https://github.com/f-lab-edu/get-offer.git'
            }

            post {
                success {
                    echo 'Preparation completed successfully!'
                }
                failure {
                    echo 'Preparation failed!'
                }
            }
        }

        stage('Build') {
            steps {
                withCredentials(bindings: [sshUserPrivateKey(credentialsId: 'JASYPT_KEY', keyFileVariable: 'JASYPT_KEY_VAR')]) {
                    sh 'JASYPT_KEY=$JASYPT_KEY_VAR ./gradlew clean build'
                }
            }

            post {
                success {
                    echo 'Build completed successfully!'
                }
                failure {
                    echo 'Build failed!'
                }
            }
        }

        stage('Upload') {
            steps {
                sh 'chmod +x ./script/upload.sh'
                sh './script/upload.sh'
            }

            post {
                success {
                    echo 'Upload completed successfully!'
                }
                failure {
                    echo 'Upload failed!'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'chmod +x ./script/deploy.sh'
                sh './script/deploy.sh'
            }

            post {
                success {
                    echo 'Deploy completed successfully!'
                }
                failure {
                    echo 'Deploy failed!'
                }
            }
        }
    }

    post {
        success {
            echo 'Build, Test, and Deploy completed successfully!'
        }
        failure {
            echo 'Build or Deploy failed!'
        }
    }
}