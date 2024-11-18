FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /build

COPY ./kafka-simple-stream /build/kafka-simple-stream
COPY ./core /build/core

RUN cd /build/core && mvn clean install && \
    cd /build/kafka-simple-stream && mvn clean package

FROM openjdk:21-slim
LABEL authors="fernandoareias"

COPY --from=build /build/kafka-simple-stream/target/kafka-simple-stream-1.0.0.jar /app/kafka-simple-stream-1.0.0.jar
WORKDIR /app

ENTRYPOINT ["java", "-jar", "kafka-simple-stream-1.0.0.jar"]
