package com.fernando.factories;


import com.fernando.CreditCardCreated;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;

public class CreditCardEventFactory {

    private static final Map<Integer, String> DOCUMENTS = Map.of(
            1, "23645754946",
            2, "59868839181",
            3, "60206973632",
            4, "50209100079",
            5, "68232380896"
    );

    public static CreditCardCreated createRandomCreditCardCreatedEvent() {
        CreditCardCreated event = new CreditCardCreated();

        String document = getRandomDocument();
        event.setPortadorDocument(document);
        event.setKey(document);

        event.setCardNumber(generateRandomCardNumber());

        return event;
    }

    private static String getRandomDocument() {
        int randomKey = ThreadLocalRandom.current().nextInt(1, DOCUMENTS.size() + 1);
        return DOCUMENTS.get(randomKey);
    }

    private static String generateRandomCardNumber() {
        Random random = new Random();
        int[] cardNumber = new int[16];

        for (int i = 0; i < 15; i++) {
            cardNumber[i] = random.nextInt(10);
        }

        cardNumber[15] = calculateLuhnCheckDigit(cardNumber);

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