package com.fernando;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ProducerKafka {
    private static final Properties properties = new Properties();
    private static final String bootstrapServers = System.getenv("KAFKA_BROKERS");
    private static final Logger logger = LoggerFactory.getLogger(ProducerKafka.class);

    private static final KafkaProducer<String, Object> producer;

    private static final Map<Class<?>, String> eventTopicMap = new HashMap<>();

    static {
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", System.getenv("SCHEMA_REGISTRY"));


        properties.setProperty(ProducerConfig.ACKS_CONFIG, "1");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024)); // 32 KB por batch
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "5");
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");

        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
        properties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, Long.toString(64 * 1024 * 1024)); // 64 MB de buffer

        producer = new KafkaProducer<>(properties);

        eventTopicMap.put(CreditCardCreated.class, "event-credit-card-created");
        eventTopicMap.put(ProposalCreatedEvent.class, "event-proposal-created");
    }

    public void sendEvent(Object event) {
        String topic = eventTopicMap.get(event.getClass());

        if (topic == null) {
            throw new IllegalArgumentException("Nenhum tópico mapeado para o tipo de evento: " + event.getClass().getName());
        }

        String key = extractKey(event);

        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topic, key, event);

        producer.send(producerRecord, handleSend());
    }

    public void finish() {
        producer.flush();
        producer.close();
    }

    private static Callback handleSend() {
        return (recordMetadata, e) -> {
            if (e == null) {
                logger.info("Mensagem enviada com sucesso para o tópico: " + recordMetadata.topic() +
                        ", partição: " + recordMetadata.partition() +
                        ", offset: " + recordMetadata.offset());
            } else {
                logger.error("Erro ao produzir", e);
            }
        };
    }

    private String extractKey(Object event) {
        if (event instanceof CreditCardCreated) {
            return ((CreditCardCreated) event).getPortadorDocument().toString();
        } else if (event instanceof ProposalCreatedEvent) {
            return ((ProposalCreatedEvent) event).getProponentDocument().toString();
        } else {
            return "default-key";
        }
    }
}
