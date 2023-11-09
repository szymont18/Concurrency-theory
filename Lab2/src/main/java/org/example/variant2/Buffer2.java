package org.example.variant2;

import org.example.IBuffer;
import org.example.Person;

/**
 * Many Consumers
 * Many Producers
 * One element buffer

 * With notify() there can be deadlock

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

public class Buffer2 implements IBuffer {

    private int buffer;
    public Buffer2(){
        buffer = 0;
    }

    public void printBufferState(){
        System.out.println("Buffer state = " + buffer + '\n');
    }

    public synchronized void consume(Person person) {

        while (buffer == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(person.introduceYourself() + " start consuming");
        buffer -= 1;
        System.out.println(person.introduceYourself() + " consumed");

        printBufferState();

        notifyAll();
    }

    public synchronized void produce(Person person){

        while (buffer == 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(person.introduceYourself() + " start producing");
        buffer += 1;
        System.out.println(person.introduceYourself() + " produced");

        printBufferState();

        notifyAll();
    }
}
