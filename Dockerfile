FROM openjdk:8-alpine
ADD target/client-api.jar client-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "client-api.jar"]