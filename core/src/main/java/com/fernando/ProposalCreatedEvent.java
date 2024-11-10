package com.fernando;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ProposalCreatedEvent extends MyEvent {

    private String proposalNumber;
    private String proponentDocument;

    private static final Map<Integer, String> _documents = new HashMap<>() {{
        put(1, "23645754946");
        put(2, "59868839181");
        put(3, "60206973632");
        put(4, "50209100079");
        put(5, "68232380896");
    }};

    public ProposalCreatedEvent(String proposalNumber, String proponentDocument) {
        setKey(proponentDocument);
        this.proposalNumber = proposalNumber;
        this.proponentDocument = proponentDocument;
    }

    public ProposalCreatedEvent() {
        this.proposalNumber = UUID.randomUUID().toString();
        this.proponentDocument = _documents.get(ThreadLocalRandom.current().nextInt(1, 6));
        setKey(proponentDocument);
    }

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public String getProponentDocument() {
        return proponentDocument;
    }

    public void setProponentDocument(String proponentDocument) {
        this.proponentDocument = proponentDocument;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ProposalCreatedEvent that = (ProposalCreatedEvent) object;
        return Objects.equals(proposalNumber, that.proposalNumber) && Objects.equals(proponentDocument, that.proponentDocument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proposalNumber, proponentDocument);
    }
}
