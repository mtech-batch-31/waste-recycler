# Build stage
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /home/gradle/app
COPY --chown=gradle:gradle . .
RUN mkdir -p /home/gradle/.gradle/caches && \
    gradle bootJar

# Test stage
FROM openjdk:17-alpine AS test
COPY --from=build /home/gradle/app/build/libs/*.jar /app.jar
RUN java -jar /app.jar --test

# Run stage
FROM openjdk:17-alpine
COPY --from=build /home/gradle/app/build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
