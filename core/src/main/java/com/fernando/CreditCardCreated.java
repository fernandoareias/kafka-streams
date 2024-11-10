package com.fernando;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CreditCardCreated  extends MyEvent{
    private String cardNumber;
    private String portadorDocument;
    private static final Map<Integer, String> _documents = new HashMap<>() {{
        put(1, "23645754946");
        put(2, "59868839181");
        put(3, "60206973632");
        put(4, "50209100079");
        put(5, "68232380896");
    }};



    public CreditCardCreated(String cardNumber, String portadorDocument) {
        this.cardNumber = cardNumber;
        this.portadorDocument = portadorDocument;
    }

    public CreditCardCreated() {
        String document =_documents.get(ThreadLocalRandom.current().nextInt(1, 6));
        setKey(document);
        cardNumber = generateRandomCardNumber();
        this.portadorDocument = document;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPortadorDocument() {
        return portadorDocument;
    }

    public void setPortadorDocument(String portadorDocument) {
        this.portadorDocument = portadorDocument;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CreditCardCreated that = (CreditCardCreated) object;
        return Objects.equals(cardNumber, that.cardNumber) && Objects.equals(portadorDocument, that.portadorDocument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, portadorDocument);
    }

    public static String generateRandomCardNumber() {
        Random random = new Random();
        int[] cardNumber = new int[16];

        // Gera os primeiros 15 dígitos aleatoriamente
        for (int i = 0; i < 15; i++) {
            cardNumber[i] = random.nextInt(10);
        }

        // Calcula o dígito de verificação usando o algoritmo de Luhn
        cardNumber[15] = calculateLuhnCheckDigit(cardNumber);

        // Converte o número do cartão para uma string
        StringBuilder cardNumberStr = new StringBuilder();
        for (int digit : cardNumber) {
            cardNumberStr.append(digit);
        }

        return cardNumberStr.toString();
    }

    private static int calculateLuhnCheckDigit(int[] cardNumber) {
        int sum = 0;
        boolean alternate = true;
        for (int i = cardNumber.length - 2; i >= 0; i--) {
            int n = cardNumber[i];
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }
}
