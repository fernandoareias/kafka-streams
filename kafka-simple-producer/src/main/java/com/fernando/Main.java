package com.fernando;

import com.fernando.factories.CreditCardEventFactory;
import com.fernando.factories.ProposalEventFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final int TARGET_RATE = Integer.parseInt(System.getenv("TARGET_RATE"));
    private static final int BATCH_SIZE = Integer.parseInt(System.getenv("BATCH_SIZE"));
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        var producer = new ProducerKafka();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        long intervalMillis = 1000L * BATCH_SIZE / TARGET_RATE;

        logger.info("Iniciando o agendador com intervalo de " + intervalMillis + " ms.");

        scheduler.scheduleAtFixedRate(() -> {
            try {
                for (int i = 0; i < BATCH_SIZE; i++) {
                    producer.sendEvent(ProposalEventFactory.createRandomProposalCreatedEvent());
                    producer.sendEvent(CreditCardEventFactory.createRandomCreditCardCreatedEvent());
                }
                logger.info("Batch de {} mensagens enviado com sucesso.", BATCH_SIZE * 2);
            } catch (Exception e) {
                logger.error("Erro ao enviar mensagens: ", e);
            }
        }, 0, intervalMillis, TimeUnit.MILLISECONDS);
    }
}
