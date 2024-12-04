FROM openjdk:17-jdk-slim
COPY build/libs/*.jar /app/myapp.jar
ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]