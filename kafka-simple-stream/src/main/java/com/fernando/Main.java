package com.fernando;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger  = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        var eventStream = new KafkaSimpleKTable();
        var privateStream = new KafkaSimpleKStream("P");
        var consignadoStream = new KafkaSimpleKStream("C");

        var threadKTable = Thread.ofPlatform().start(eventStream::startStream);
        var threadStreamPrivate = Thread.ofPlatform().start(privateStream::startStream);
        var threadStreamConsignado = Thread.ofPlatform().start(consignadoStream::startStream);

        try {
           threadKTable.join();
            threadStreamPrivate.join();
            threadStreamConsignado.join();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}