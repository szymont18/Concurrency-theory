package org.example;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiElementBuffer2 implements IBuffer {
    /*
    buffer = 0 means that there is NOTHING to consume;
    buffer = 1 means that there is SOMETHING to consume
     */
    private int buffer;
    private final int maxBuffer;

    private final Lock lock;
    private final Condition consumerCondition;
    private final Condition producerCondition;


    public MultiElementBuffer2(int maxBuffer){
        this.buffer = 0;
        this.maxBuffer = maxBuffer;

        this.lock = new ReentrantLock();
        this.consumerCondition = lock.newCondition();
        this.producerCondition = lock.newCondition();
    }


    public void printBufferState(){
        System.out.println("Buffer state = " + buffer);
        System.out.println();
    }
    public void consume(Person person){
        try {
            this.lock.lock();

            while (buffer == 0) {
                this.consumerCondition.await();
            }


            System.out.println(person.introduceYourself() + " start consuming");

            buffer -= 1;
            System.out.println(person.introduceYourself() + " consumed");

            printBufferState();

            this.producerCondition.signal();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.lock.unlock();
        }
    }

    public void produce(Person person){
        try {
            this.lock.lock();

            while (buffer == this.maxBuffer) {
                this.producerCondition.await();
            }

            System.out.println(person.introduceYourself() + " start producing");


            buffer += 1;
            System.out.println(person.introduceYourself() + " produced");

            printBufferState();

            this.consumerCondition.signal();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            this.lock.unlock();
        }
    }

    @Override
    public void consume(Consumer person, int request) {

    }

    @Override
    public void produce(Producer person, int request) {

    }

}
