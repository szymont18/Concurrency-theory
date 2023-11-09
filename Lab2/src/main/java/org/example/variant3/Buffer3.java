package org.example.variant3;

import org.example.IBuffer;
import org.example.Person;

import java.util.Random;

/**
 * Many Consumer
 * Many Producer
 * Many element Buffer
 * Deadlock - Free
 */
public class Buffer3 implements IBuffer {
    private int buffer;
    private final int maxBuffer;
    private final Random randomSleep = new Random();

    public Buffer3(int MAX_BUFFER){
        buffer = 0;
        maxBuffer = MAX_BUFFER;
    }


    public long getSleepTime(){
        int randomTime = randomSleep.nextInt(3);
        return randomTime * 1000;
    }

    public void printBufferState(){
        System.out.println("Buffer state = " + buffer);
    }

    public synchronized void consume(Person person)  {

        while (buffer == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        long sleepSeconds = getSleepTime();

//        System.out.println(person.introduceYourself() + " start consuming");

//        Thread.sleep(sleepSeconds);

        System.out.println(person.introduceYourself() + " consumed " + buffer);
        buffer -= 1;

//        printBufferState();

        notifyAll();
    }

    public synchronized void produce(Person person){

        while (buffer == maxBuffer) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long sleepSeconds = getSleepTime();

//        System.out.println(person.introduceYourself() + " start producing");

//        Thread.sleep(sleepSeconds);
        buffer += 1;
        System.out.println(person.introduceYourself() + " produced " + buffer);


//        printBufferState();

        notifyAll();

    }
}
