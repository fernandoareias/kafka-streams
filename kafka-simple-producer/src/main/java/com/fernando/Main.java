package com.fernando;


import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.Thread.ofVirtual;

public class Main {


    public static void main(String[] args) {

        for(int x = 0; x < 20; x++){
            ProducerKafka producerProposal = new ProducerKafka("event-proposal-created", new ProposalCreatedEvent());
            ProducerKafka producerCreditCard = new ProducerKafka("event-credit-card-created", new CreditCardCreated());

            producerProposal.start();
            producerCreditCard.start();

            producerProposal.finish();
            producerCreditCard.finish();
        }
//
//        Thread thread1 = ofVirtual().start(producerProposal::start);
//        Thread thread2 = ofVirtual().start(producerCreditCard::start);
//
//        try {
//            thread1.join();
//            thread2.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//

    }

}