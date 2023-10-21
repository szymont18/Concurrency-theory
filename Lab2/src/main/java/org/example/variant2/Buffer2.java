package org.example.variant2;

import org.example.IBuffer;
import org.example.Person;


public class Buffer2 implements IBuffer {
    /*
    buffer = 0 means that there is NOTHING to consume;
    buffer = 1 means that there is SOMETHING to consume
     */
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
