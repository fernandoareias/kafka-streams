package com.fernando;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Properties;

public class KafkaSimpleStream {
    private static final String bootstrapServers = "127.0.0.1:19090,127.0.0.1:19091,127.0.0.1:19092";

    public void startStream(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "simple-kafka-stream");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // Serdes personalizados para os tipos específicos
        JsonSerde<ProposalCreatedEvent> proposalSerde = new JsonSerde<>(ProposalCreatedEvent.class);
        JsonSerde<CreditCardCreated> creditCardSerde = new JsonSerde<>(CreditCardCreated.class);
        JsonSerde<ClientCreatedEvent> clientCreatedSerde = new JsonSerde<>(ClientCreatedEvent.class);

        StreamsBuilder builder = new StreamsBuilder();

        // Consome o tópico como KTable com Serde customizado
        KTable<String, ProposalCreatedEvent> proposalTable = builder.table(
                "event-proposal-created",
                Consumed.with(Serdes.String(), proposalSerde)
        );

        KTable<String, CreditCardCreated> creditCardTable = builder.table(
                "event-credit-card-created",
                Consumed.with(Serdes.String(), creditCardSerde)
        );

        // Realiza o join das duas tabelas com base no Documento
        KStream<String, ClientCreatedEvent> newClientStream = proposalTable.join(
                creditCardTable,
                (proposal, creditCard) -> new ClientCreatedEvent(
                        creditCard.getCardNumber(),
                        proposal.getProposalNumber(),
                        creditCard.getPortadorDocument()
                ),
                Materialized.with(Serdes.String(), clientCreatedSerde)
        ).toStream();

        newClientStream.to("event-client-created", Produced.with(Serdes.String(), clientCreatedSerde));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

}
