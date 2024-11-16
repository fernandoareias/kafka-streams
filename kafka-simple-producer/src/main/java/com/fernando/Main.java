package com.fernando;


import com.fernando.factories.CreditCardEventFactory;
import com.fernando.factories.ProposalEventFactory;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.Thread.ofVirtual;

public class Main {
    public static void main(String[] args) {
        var producer = new ProducerKafka();

        for(int x = 0; x < 20; x++){
            producer.sendEvent(ProposalEventFactory.createRandomProposalCreatedEvent());
            producer.sendEvent(CreditCardEventFactory.createRandomCreditCardCreatedEvent());
        }
    }

}