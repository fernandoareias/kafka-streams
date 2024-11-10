package com.fernando;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ClientCreatedEvent extends MyEvent {
    private String cardNumber;
    private String proposalNumber;
    private String document;


    public ClientCreatedEvent(String cardNumber, String proposalNumber, String document) {
        setKey(document);
        this.cardNumber = cardNumber;
        this.proposalNumber = proposalNumber;
        this.document = document;
    }

    public ClientCreatedEvent() {

    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ClientCreatedEvent that = (ClientCreatedEvent) object;
        return Objects.equals(cardNumber, that.cardNumber) && Objects.equals(proposalNumber, that.proposalNumber) && Objects.equals(document, that.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, proposalNumber, document);
    }
}
