pipeline {
    agent any

    environment {
        TARGET_HOST = "ubuntu@13.125.29.139"
        RESOURCE_PATH = "/home/ubuntu/jenkins/workspace/bs-back/src/main/resources"
    }

    stages {

        stage('Get Secret File') {
            steps {
                sshagent (credentials: ['ec2-ssh-key']) {
                    script {
                        // 비밀 파일을 작업 공간으로 가져오기
                        withCredentials([file(credentialsId: 'application-prod', variable: 'PROD_YML'), file(credentialsId: 'application-secret', variable: 'SECRET_YML')]) {
                            // 비밀 파일을 대상 서버로 전송
                            sh 'scp -o StrictHostKeyChecking=no $PROD_YML ${TARGET_HOST}:${RESOURCE_PATH}'
                            sh 'scp -o StrictHostKeyChecking=no $SECRET_YML ${TARGET_HOST}:${RESOURCE_PATH}'
                        }
                    }
                }
            }
        }

        stage('Build JAR') {
            steps {
                sh '''
                    chmod +x ./gradlew
                    ./gradlew clean test bootJar
                '''
            }
        }

        stage('Execute deploy.sh') {
            steps {
                sshagent (credentials: ['ec2-ssh-key']) {
                    script {
                        def host = "${TARGET_HOST}"
                        sh """#!/bin/bash
                        # EC2 서버에서 deploy.sh 실행
                        ssh -o StrictHostKeyChecking=no '${host}' '
                          cd /home/ubuntu &&
                          ./deploy.sh
                        '
                        """
                    }
                }
            }
        }

        stage('Deploy bserver-1') {
            steps {
                sshagent (credentials: ['ec2-ssh-key']) {
                    script {
                        def host = "${TARGET_HOST}"
                        sh """#!/bin/bash
                        ssh -o StrictHostKeyChecking=no '${host}' '
                          cd /home/ubuntu &&
                          docker-compose stop bserver-1 || true &&
                          docker-compose rm -f bserver-1 || true &&
                          docker-compose up -d bserver-1
                        '
                        """
                    }
                }
            }
        }

        stage('Deploy bserver-2') {
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            steps {
                sshagent (credentials: ['ec2-ssh-key']) {
                    script {
                        def host = "${TARGET_HOST}"
                        sh """#!/bin/bash
                        ssh -o StrictHostKeyChecking=no '${host}' '
                          cd /home/ubuntu &&
                          docker-compose stop bserver-2 || true &&
                          docker-compose rm -f bserver-2 || true &&
                          docker-compose up -d bserver-2
                        '
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "서버 재배포 완료"
        }
        failure {
            echo "서버 재배포 실패"
        }
    }
}
