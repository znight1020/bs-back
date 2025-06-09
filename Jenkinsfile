pipeline {
    agent any

    environment {
        DOCKER_USERNAME = credentials('docker-hub-id')
        DOCKER_PASSWORD = credentials('docker-hub-password')
        EC2_USER = credentials('ec2-user')
        EC2_HOST = credentials('ec2-host')
        TARGET_HOST = "${EC2_USER}@${EC2_HOST}"
        JIB_APP_CACHE   = "/var/jenkins_home/.jib-cache/app"
        JIB_BASE_CACHE  = "/var/jenkins_home/.jib-cache/base"
    }

    stages {

        stage('Get RESOURCE_PATH') {
            steps {
                withCredentials([string(credentialsId: 'api-server-workspace-path', variable: 'REMOTE_RESOURCE_PATH')]) {
                    script {
                        env.RESOURCE_PATH = "${REMOTE_RESOURCE_PATH}/src/main/resources"
                    }
                }
            }
        }

        stage('Get SecretFile') {
            steps {
                sshagent (credentials: ['ec2-ssh-key']) {
                    withCredentials([
                        file(credentialsId: 'application-prod', variable: 'PROD_YML'),
                        file(credentialsId: 'application-secret', variable: 'SECRET_YML'),
                        file(credentialsId: 'application-dev', variable: 'DEV_YML')
                    ]) {
                        sh 'scp -o StrictHostKeyChecking=no $PROD_YML ${TARGET_HOST}:${RESOURCE_PATH}'
                        sh 'scp -o StrictHostKeyChecking=no $SECRET_YML ${TARGET_HOST}:${RESOURCE_PATH}'
                        sh 'scp -o StrictHostKeyChecking=no $DEV_YML ${TARGET_HOST}:${RESOURCE_PATH}'
                    }
                }
            }
        }

        stage('Run Test') {
            steps {
                sh '''
                    chmod +x ./gradlew
                    ./gradlew clean test
                '''
            }
        }

        stage('JIB Build & Push') {
            steps {
                sh """
                    ./gradlew jib \
                        -Djib.to.auth.username=$DOCKER_USERNAME \
                        -Djib.to.auth.password=$DOCKER_PASSWORD \
                        -Djib.from.auth.username=$DOCKER_USERNAME \
                        -Djib.from.auth.password=$DOCKER_PASSWORD \
                        -Djib.applicationCache=$JIB_APP_CACHE \
                        -Djib.baseImageCache=$JIB_BASE_CACHE
                """
            }
        }

        stage('Deploy Container') {
            steps {
                sshagent (credentials: ['ec2-ssh-key']) {
                    script {
                        def services = ['bserver-1', 'bserver-2', 'bserver-dev']
                        for (svc in services) {
                            sh """
                                ssh -o StrictHostKeyChecking=no ${TARGET_HOST} '
                                  docker pull leehyeonsoo/bserver:latest &&
                                  docker-compose stop ${svc} || true &&
                                  docker-compose rm -f ${svc} || true &&
                                  docker-compose up -d ${svc}
                                '
                            """
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo "서버 배포 완료"
        }
        failure {
            echo "서버 배포 실패"
        }
    }
}
