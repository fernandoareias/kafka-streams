# Etapa de build para o kafka-simple-producer
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /build

# Copia o módulo core e o kafka-simple-producer
COPY ./core /build/core
COPY ./kafka-simple-producer /build/kafka-simple-producer

# Primeiro, compila e instala o core para o repositório local do Maven
RUN cd /build/core && mvn clean install

# Em seguida, compila o kafka-simple-producer
RUN cd /build/kafka-simple-producer && mvn clean package

# Imagem final
FROM openjdk:21-slim
LABEL authors="fernandoareias"
WORKDIR /app

# Copia o JAR do kafka-simple-producer da etapa de build
COPY --from=build /build/kafka-simple-producer/target/kafka-simple-producer-1.0.0.jar /app/kafka-simple-producer-1.0.0.jar

# Define o ponto de entrada para o aplicativo producer
ENTRYPOINT ["java", "-jar", "kafka-simple-producer-1.0.0.jar"]
