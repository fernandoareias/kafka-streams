package com.fernando.factories;


import com.fernando.ProposalCreatedEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ProposalEventFactory {

    private static final Map<Integer, String> DOCUMENTS = Map.of(
            1, "23645754946",
            2, "59868839181",
            3, "60206973632",
            4, "50209100079",
            5, "68232380896"
    );

    private static final String[] PRODUCTS = {"P", "C"};

    public static ProposalCreatedEvent createRandomProposalCreatedEvent() {
        ProposalCreatedEvent event = new ProposalCreatedEvent();

        event.setProposalNumber(UUID.randomUUID().toString());
        event.setProponentDocument(getRandomDocument());
        event.setProduct(getRandomProduct());
        event.setKey(event.getProponentDocument());

        return event;
    }

    private static String getRandomDocument() {
        int randomKey = ThreadLocalRandom.current().nextInt(1, DOCUMENTS.size() + 1);
        return DOCUMENTS.get(randomKey);
    }

    private static String getRandomProduct() {
        int randomIndex = ThreadLocalRandom.current().nextInt(PRODUCTS.length);
        return PRODUCTS[randomIndex];
    }
}