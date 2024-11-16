package com.fernando;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaSimpleKTable {
    private static final String bootstrapServers = "127.0.0.1:19090,127.0.0.1:19091,127.0.0.1:19092";
    private static final Logger logger = LoggerFactory.getLogger(KafkaSimpleKTable.class);
    private static final Properties props = new Properties();
    private static final SpecificAvroSerde<ProposalCreatedEvent> proposalSerde = new SpecificAvroSerde<>();
    private static final SpecificAvroSerde<CreditCardCreated> creditCardSerde = new SpecificAvroSerde<>();
    private static final SpecificAvroSerde<ClientCreatedEvent> clientCreatedSerde = new SpecificAvroSerde<>();

    static {
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "simple-kafka-ktable2");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 1);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                LogAndContinueExceptionHandler.class);
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/Users/fernandoareias/Documents/dev/kafka-stream/infrastructure/stream");
        props.put("allow.auto.create.topics", "false");

        final Map<String, String> serdeConfig = new HashMap<>();
        serdeConfig.put("schema.registry.url", "http://localhost:8081");
        serdeConfig.put("auto.register.schemas", "true");

        proposalSerde.configure(serdeConfig, false);
        creditCardSerde.configure(serdeConfig, false);
        clientCreatedSerde.configure(serdeConfig, false);
    }

    public void startStream() {
        StreamsBuilder builder = new StreamsBuilder();

        KTable<String, ProposalCreatedEvent> proposalTable = builder.table(
                "event-proposal-created",
                Consumed.with(Serdes.String(), proposalSerde)
        );

        proposalTable.toStream().foreach((key, value) -> logger.info("KTable Key=" + key + ", Value=" + value));


        KStream<String, CreditCardCreated> creditCardStream = builder.stream(
                "event-credit-card-created",
                Consumed.with(Serdes.String(), creditCardSerde)
        );

        creditCardStream.foreach((key, value) -> logger.info("KStream Key=" + key + ", Value=" + value));


        KStream<String, ClientCreatedEvent> clientCreatedEventKStream = creditCardStream.leftJoin(
                proposalTable,
                (creditCard, proposal) -> {
                    logger.info("Join attempt - creditCard: {}, proposal: {}", creditCard, proposal);

                    if (creditCard != null && proposal != null) {
                        ClientCreatedEvent event = new ClientCreatedEvent();
                        event.setKey(creditCard.getPortadorDocument().toString());
                        event.setCardNumber(creditCard.getCardNumber().toString());
                        event.setProposalNumber(proposal.getProposalNumber().toString());
                        event.setDocument(creditCard.getPortadorDocument().toString());
                        event.setProduct(proposal.getProduct().toString());
                        return event;
                    }
                    return null;
                },
                Joined.with(Serdes.String(), creditCardSerde, proposalSerde)
        );


        clientCreatedEventKStream
                .foreach((key, value) ->
                        logger.info("ClientCreatedEvent Key=" + key + ", Value=" + value)
                );

        clientCreatedEventKStream.to("event-client-created", Produced.with(Serdes.String(), clientCreatedSerde));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
