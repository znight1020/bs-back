# base image
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 jar 복사
COPY build/libs/*.jar app.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
