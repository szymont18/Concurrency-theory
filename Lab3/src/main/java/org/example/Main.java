package org.example;

import org.example.conditions2.RandomBuffer;
import org.example.conditions4.BufferHasWaiters;
import org.example.conditions4.StarvationFreeBuffer;

public class Main {
    public static void main(String[] args){
        int noConsumer = 4;
        int noProducer = 4;
        int noRound = 10;
        int timeBetweenRounds = 5000; // milliseconds

        // Consumer 1 is one who has to starve.

        // Choose buffer type:
//        RandomBuffer buffer = new RandomBuffer(50);
        StarvationFreeBuffer buffer = new StarvationFreeBuffer(50);
//        BufferHasWaiters buffer = new BufferHasWaiters(50);

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

        for(int round = 0; round < noRound; round++){
            System.out.println("Round " + (round+1));
            for(int i = 0; i < noConsumer; i++){
                System.out.println("Consumer " + (i + 1) + " consumed " + consumers[i].getNoConsumed() + " times");
            }
            System.out.println();
            // Wait some time
            try {
                Thread.sleep(timeBetweenRounds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
          }

    }
}