pipeline {
    agent any
    environment {
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
                sh 'JASYPT_KEY=$JASYPT_KEY ./gradlew clean build'
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
        environment {
         dockerHubRegistry = 'https://registry.hub.docker.com'
         dockerRepository = 'soleu/get-offer'
        }

        stage('Docker image build') {
           steps {
                 sh "docker build -t ${dockerRepository}:${currentBuild.number} ."
                 sh "docker build -t ${dockerRepository}:latest ."
                 sh "docker rmi ${dockerRepository}:${currentBuild.number}"
             }
             post {
                     failure {
                       echo 'Docker image build failure!'
                     }
                     success {
                       echo 'Docker image build success!'
                     }
             }
        }

        stage('Docker Image Push') {
            steps {
                withCredentials([[$class: 'UsernamePasswordMultiBinding',
                credentialsId: 'dockerhub-credentials',
                usernameVariable: 'DOCKER_USER_ID',
                passwordVariable: 'DOCKER_USER_PASSWORD'
                ]]){
                    sh 'echo ${DOCKER_USER_PASSWORD} | docker login -u ${DOCKER_USER_ID} --password-stdin ${dockerHubRegistry}'
                    sh "docker push ${dockerRepository}:latest"
                }
            }
        }

      stage('Server Docker Run') {
          steps {
              sshagent (credentials: ['jenkins-rsa']) {
                  sh '''
                      #!/bin/bash

                      if curl -s "${blue_url}" > /dev/null
                      then
                          deployment_container_name=green
                          deployment_target_ip=$green_url
                          deployment_port=$green_port
                          old_container_name=blue
                          echo ${blue_url}
                      else
                          deployment_container_name=blue
                          deployment_target_ip=$blue_url
                          deployment_port=$blue_port
                          old_container_name=green
                      fi

                      ssh root@${deploy_ip} "nohup docker run --name ${deployment_container_name} -p ${deployment_port}:8080 ${dockerRepository}:latest > /dev/null &" &

                      for retry_count in \$(seq 10)
                      do
                          if curl -s "${deployment_target_ip}" > /dev/null
                          then
                              echo "서버 Health Check에 성공했습니다."
                              break
                          fi

                          if [ $retry_count -eq 10 ]
                          then
                              echo "서버 Health Check에 실패했습니다."
                              exit 1
                          fi

                          echo "서버 Health Check를 10초 이후에 재시도합니다..."
                          sleep 10
                      done

                      ssh root@${deploy_ip} "echo 'set \\\$service_url ${deployment_target_ip};' > /root/docker-compose/nginx/nginx/conf.d/service-url.inc"
                      ssh root@${deploy_ip} "docker exec -i nginx service nginx reload"
                      echo "Nginx Reverse Proxy 변경: ${deployment_target_ip}"

                      echo "기존 도커 서버 종료"
                      ssh root@${deploy_ip} "docker rm -f ${old_container_name}"
                  '''
              }
          }
      }
   }
}