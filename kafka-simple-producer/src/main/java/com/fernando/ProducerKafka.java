package com.fernando;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerKafka {
    private static final Properties properties = new Properties();
    private static final String bootstrapServers = "127.0.0.1:19090,127.0.0.1:19091,127.0.0.1:19092";
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static KafkaProducer<String, String> producer;
    private String topic;
    private MyEvent event;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProducerKafka(String topic, MyEvent event) {
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        properties.setProperty(ProducerConfig.ACKS_CONFIG, "1");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

       producer = new KafkaProducer<>(properties);
       this.topic = topic;
       this.event = event;

    }

    public void start(){

       try  {
           String jsonEvent = objectMapper.writeValueAsString(event);
           logger.info(String.format("Message: %s", jsonEvent));
           ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, event.getKey(), jsonEvent);
           producer.send(producerRecord, handleSend());


       }catch (Exception e){

       }
    }

    public void finish(){
        producer.flush();
        producer.close();
    }

    private static Callback handleSend(){
        return new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e == null) {
                    logger.info("Received new metadata. " +
                            "Topic:" + recordMetadata.topic() +
                            "Partition: " + recordMetadata.partition() +
                            "Offset: " + recordMetadata.offset() +
                            "Timestamp: " + recordMetadata.timestamp());
                } else {
                    logger.error("Error while producing", e);
                }
            }
        };
    }
}
