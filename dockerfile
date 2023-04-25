FROM openjdk:17-alpine
COPY src/main/resources/application*.properties /app/config/
COPY build/libs/recycler.jar /recycler.jar
EXPOSE 8080
CMD ["java", "-jar", "/recycler.jar", "--spring.config.location=/app/config/"]
