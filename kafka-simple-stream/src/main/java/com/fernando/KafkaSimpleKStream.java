package com.fernando;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaSimpleKStream {

    private static final String bootstrapServers = "127.0.0.1:19090,127.0.0.1:19091,127.0.0.1:19092";
    private static final String topic = "event-client-created";
    private String productType;

    private static final Map<String, String> outputTopic = new HashMap<>(){{
        put("C", "client-consignado");
        put("P", "client-private");
    }};



    public KafkaSimpleKStream(String productType) {
        this.productType = productType;
    }

    public void startStream(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "simple-kafka-stream2");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());


        JsonSerde<ClientCreatedEvent> clientCreatedSerde = new JsonSerde<>(ClientCreatedEvent.class);

        StreamsBuilder builder = new StreamsBuilder();

        builder.stream(topic, Consumed.with(Serdes.String(), clientCreatedSerde))
                .filter((k, v) -> v != null && v.getProduct().equals(productType))
                .peek((key, value) -> System.out.println(
                        String.format("Consuming event by product type %s, key %s and value %s", productType, key, value)
                ))
                .to(outputTopic.get(productType), Produced.with(Serdes.String(), clientCreatedSerde));


        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
