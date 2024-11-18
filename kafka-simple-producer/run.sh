#!/bin/bash

export TARGET_RATE=2000
export BATCH_SIZE=10
export KAFKA_BROKERS="kafka01:19090,kafka02:19091,kafka03:19092"
export SCHEMA_REGISTRY="schema-registry:8081"

JAR_PATH="target/kafka-simple-producer-1.0.0.jar"

if [ ! -f "$JAR_PATH" ]; then
    echo "Erro: $JAR_PATH não encontrado. Certifique-se de que o projeto foi compilado com 'mvn package'."
    exit 1
fi

echo "Iniciando a aplicação com TARGET_RATE=$TARGET_RATE e BATCH_SIZE=$BATCH_SIZE..."
java -jar "$JAR_PATH"
