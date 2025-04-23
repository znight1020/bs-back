pipeline {
    agent any

    environment {
        HOST = "ubuntu@13.125.29.139"
        DEPLOY_DIR = "/home/ubuntu"
    }

    stages {
        stage('Build JAR') {
            steps {
                sh '''
                    chmod +x ./gradlew
                    ./gradlew clean test bootJar
                '''
            }
        }

        stage('Deploy to EC2 via SSH') {
            steps {
                sshagent (credentials: ['ec2-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ubuntu@13.125.29.139 "
                      cd /home/ubuntu && \
                      docker-compose stop bserver-1 bserver-2 || true && \
                      docker-compose rm -f bserver-1 bserver-2 || true && \
                      docker-compose build bserver-1 bserver-2 && \
                      docker-compose up -d bserver-1 bserver-2
                    "
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "✅ 서버 재배포 완료!"
        }
        failure {
            echo "❌ 재배포 실패!"
        }
    }
}
