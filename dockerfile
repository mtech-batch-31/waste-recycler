FROM openjdk:17-alpine
COPY --from=build build/libs/recycler.jar /recycler.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/recycler.jar"]
