# Build stage
FROM gradle:7.6.1-jdk17 AS build
RUN mkdir -p /home/runner/.gradle/wrapper
COPY --chown=gradle:gradle . /home/gradle/app
WORKDIR /home/gradle/app
RUN gradle bootJar

# Test stage
FROM openjdk:17-alpine AS test
COPY --from=build /home/gradle/app/build/libs/*.jar /app.jar
RUN java -jar /app.jar --test

# Run stage
FROM openjdk:17-alpine
COPY --from=build /home/gradle/app/build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
