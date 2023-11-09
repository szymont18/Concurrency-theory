package org.example.variant1;

import org.example.IBuffer;
import org.example.Person;

import java.util.Random;

/**
 * One Consumer
 * One Producer
 * One element Buffer
 */
public class Buffer1 implements IBuffer {
    /*
    buffer = 0 means that there is NOTHING to consume;
    buffer = 1 means that there is SOMETHING to consume
     */
    private int buffer;


    public Buffer1(){
        buffer = 0;
    }



    public void printBufferState(){
        System.out.println("Buffer state = " + buffer);
        System.out.println();
    }
    public synchronized void consume(Person person){
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

        notify();
    }

    public synchronized void produce(Person person){
        while (buffer == 1){
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

        notify();
    }

}
