FROM openjdk:17-jdk-slim
COPY api/build/libs/*.jar /app/myapp.jar
ENV SPRING_PROFILE prod
ENV JASYPT_KEY=gosol
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "/app/myapp.jar"]