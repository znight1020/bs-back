FROM openjdk:17-jdk-slim
WORKDIR /app

COPY build/libs/*.jar /home/ubuntu/app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
