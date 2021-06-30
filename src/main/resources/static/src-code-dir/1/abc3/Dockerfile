FROM openjdk:8
ADD client-api.jar  /home/client-api.jar
EXPOSE 8080 			#Add server.port = 8085 to application.properties
ENTRYPOINT ["java", "-jar", "/home/client-api.jar"]
