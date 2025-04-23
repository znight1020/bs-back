pipeline {
    agent any

    environment {
        HOST = "ubuntu@13.125.29.139"
    }

    stages {
        stage('Deploy bserver-1') {
            steps {
                sshagent (credentials: ['ec2-ssh-key']) {
                    script {
                        def host = "${HOST}"
                        sh """#!/bin/bash
                        ssh -o StrictHostKeyChecking=no '${host}' '
                          cd /home/ubuntu &&
                          docker-compose stop bserver-1 || true &&
                          docker-compose rm -f bserver-1 || true &&
                          docker-compose build bserver-1 &&
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
                        def host = "${HOST}"
                        sh """#!/bin/bash
                        ssh -o StrictHostKeyChecking=no '${host}' '
                          cd /home/ubuntu &&
                          docker-compose stop bserver-2 || true &&
                          docker-compose rm -f bserver-2 || true &&
                          docker-compose build bserver-2 &&
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
