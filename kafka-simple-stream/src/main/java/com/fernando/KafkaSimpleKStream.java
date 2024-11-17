package com.fernando;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.errors.LogAndFailExceptionHandler;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaSimpleKStream {

    private static final String bootstrapServers = "127.0.0.1:19090,127.0.0.1:19091,127.0.0.1:19092";
    private static final Logger logger = LoggerFactory.getLogger(KafkaSimpleKStream.class);
    private static final Properties props = new Properties();
    private static final SpecificAvroSerde<ClientCreatedEvent> clientCreatedSerde = new SpecificAvroSerde<>();

    private static final String topic = "event-client-created";

    private final String productType;

    public KafkaSimpleKStream(String productType) {
        logger.info("Current productType: {}", productType);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "simple-kafka-stream-" + productType);
        this.productType = productType;
    }

    static {

        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 20);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                LogAndFailExceptionHandler.class);

        props.put(StreamsConfig.STATE_DIR_CONFIG, "/Users/fernandoareias/Documents/dev/kafka-stream/infrastructure/stream");
        props.put("allow.auto.create.topics", "true");

        final Map<String, String> serdeConfig = new HashMap<>();
        serdeConfig.put("schema.registry.url", "http://localhost:8081");
        serdeConfig.put("auto.register.schemas", "true");

        clientCreatedSerde.configure(serdeConfig, false);
    }


    private static final Map<String, String> outputTopic = new HashMap<>(){{
        put("C", "event-new-client-consignado");
        put("P", "event-new-client-private");
    }};

    public void startStream(){

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, ClientCreatedEvent> clientCreatedEventKStream = builder.stream("event-client-created", Consumed.with(Serdes.String(), clientCreatedSerde));


        clientCreatedEventKStream
                .filter((k, v) -> v != null && v.getProduct().toString().equals(productType))
                .peek((key, value) -> logger.info(
                        String.format("Consuming event by product type %s, key %s and value %s", productType, key, value)
                )).to(outputTopic.get(productType), Produced.with(Serdes.String(), clientCreatedSerde));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
