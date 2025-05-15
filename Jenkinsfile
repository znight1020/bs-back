pipeline {
    agent any

    environment {
        TARGET_HOST = "ubuntu@13.125.29.139"
        RESOURCE_PATH = "/home/ubuntu/jenkins/workspace/bob-back/src/main/resources"
    }

    stages {
        stage('Get Secret File') {
            steps {
                sshagent (credentials: ['ec2-ssh-key']) {
                    script {
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
                        sh """
                        ssh -o StrictHostKeyChecking=no '${host}' '
                          cd /home/ubuntu &&
                          ./deploy.sh
                        '
                        """
                    }
                }
            }
        }

        stage('Deploy Services') {
            steps {
                script {
                    def services = ['bserver-1', 'bserver-2', 'bserver-dev']

                    sshagent (credentials: ['ec2-ssh-key']) {
                        for (svc in services) {
                            sh """
                            ssh -o StrictHostKeyChecking=no ${TARGET_HOST} '
                              cd /home/ubuntu &&
                              docker-compose stop ${svc} || true &&
                              docker-compose rm -f ${svc} || true &&
                              docker rmi -f ubuntu_${svc} || true &&
                              docker-compose build --no-cache ${svc} &&
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
            echo "서버 재배포 완료"
        }
        failure {
            echo "서버 재배포 실패"
        }
    }
}
