package com.fernando;

public class Main {
    public static void main(String[] args) {
        KafkaSimpleKTable ktable = new KafkaSimpleKTable();
//        KafkaSimpleKStream kstreamPrivate = new KafkaSimpleKStream("P");
//        KafkaSimpleKStream kstreamConsignado = new KafkaSimpleKStream("C");

        //var threadKTable = Thread.ofVirtual().start(ktable::startStream);
//        var threadStreamPrivate = Thread.ofVirtual().start(kstreamPrivate::startStream);
//        var threadStreamConsignado = Thread.ofVirtual().start(kstreamConsignado::startStream);

        try {
            ktable.startStream();
            //threadKTable.join();
//            threadStreamPrivate.join();
//            threadStreamConsignado.join();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}