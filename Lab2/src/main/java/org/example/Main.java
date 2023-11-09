package org.example;

import org.example.exercises.*;
import org.example.variant1.Buffer1;
import org.example.variant2.Buffer2;


public class Main {
    public static void main(String[] args) {

        /*
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
        */

        // Exercise 1 - Many Producers, many Consumers, many buffers and many request.
        /*
        int numberOfProducers = 5;
        int numberOfConsumers = 5;

        IBuffer buffer = new RandomBuffer(20);

        for(int i = 0; i < numberOfProducers; i++){
            Producer producer = new Producer(i + 1, buffer);
            producer.start();
        }

        for(int i = 0; i < numberOfConsumers; i++) {
            Consumer consumer = new Consumer(i + 1, buffer);
            consumer.start();
        }
        */

        // Exercise 2 - Readers and Writers
        /*
        int numberOfReaders = 3;
        int numberOfWriters = 3;

        Library library = new Library();
        for(int i = 0; i < numberOfReaders; i++){
            Reader reader = new Reader(i + 1, library);
            reader.start();
        }

        for(int i = 0; i < numberOfWriters; i++) {
            Writer writer = new Writer(i + 1, library);
            writer.start();
        }

         */

        // Exercise 3 - 5 philosophers
        int noPhilosophers = 5;

        Forks forks = new Forks(noPhilosophers);

        for(int i = 0; i < noPhilosophers; i++){
            Philosopher philosopher = new Philosopher(i + 1, forks);
            philosopher.start();
        }

    }
}