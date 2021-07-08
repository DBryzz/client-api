FROM openjdk:8-alpine
COPY . /src/java
WORKDIR /src/java
RUN ["javac", "Pow.java"]
ENTRYPOINT ["java", "Pow"]
