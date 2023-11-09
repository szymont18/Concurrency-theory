package org.example;

public class Main {
    public static void main(String[] args) {
        int noConsumer = 4;
        int noProducer = 3;

//        RandomBuffer buffer = new RandomBuffer(20);

        StarvationFreeBuffer buffer = new StarvationFreeBuffer(20);

        Consumer[] consumers = new Consumer[noConsumer];

        for(int i = 0; i < noConsumer; i++){
            Consumer consumer = new Consumer(i+1, buffer);
            consumers[i] = consumer;
            consumer.start();
        }

        for (int i = 0; i < noProducer; i++){
            Producer producer = new Producer(i + 1, buffer);
            producer.start();
        }

        while(true){
            for(int i = 0; i < noConsumer; i++){
                System.out.println("Consumer " + (i + 1) + " consumed " + consumers[i].getNoConsumed() + " times");
            }
            System.out.println();
//
        }


    }
}