FROM openjdk:17-alpine
COPY build/libs/recycler.jar /recycler.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/recycler.jar"]
