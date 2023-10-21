package org.example;

import org.example.variant1.Buffer1;
import org.example.variant2.Buffer2;


public class Main {
    public static void main(String[] args) {

        /*
        Previous version: Deadlock
        Threads: P1, P2 (producer); C1 (consumer)

        1) Buffor value = 0;
           wait:
           queue: {P1, P2, C1}

        2) Buffor value = 1;
           wait:
           queue: {P1, P2, C1}

        3) Buffor value = 1;
           wait: {P2}
           queue: {P1, C1}

        4) Buffor value = 1;
           wait: {P1, P2}
           queue: {C1}

        5) Buffor value = 0;
           wait: {P2}
           queue: {P1, C1}

        6) Buffor value = 0;
           wait: {P2, C1}
           queue: {P1}

        7) Buffor value = 1;
           wait: {C1}
           queue: {P1, P2}

        8) Buffor value = 1;
           wait: {C1, P1}
           queue: {P2}

        9) Buffor value = 1;
           wait: {P1, P2, C1}
           queue:
         */

        int numberOfProducers = 10;
        int numberOfConsumers = 10;


        Buffer2 buffer = new Buffer2();

        for(int i = 0; i < numberOfProducers; i++){
            Producer producer = new Producer(i + 1, buffer);
            producer.start();
        }

        for(int i = 0; i < numberOfConsumers; i++) {
            Consumer consumer = new Consumer(i + 1, buffer);
            consumer.start();
        }


    }
}