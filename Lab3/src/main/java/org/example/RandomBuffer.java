package org.example;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RandomBuffer implements  IBuffer{
    private int buffer;
    private final int maxBuffer;

    private final Lock lock;
    private final Condition consumerCondition;
    private final Condition producerCondition;


    public RandomBuffer(int maxBuffer){
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



    public void consume(Consumer person, int request){
        try {
            this.lock.lock();

            while ((buffer - request) < 0) {
                this.consumerCondition.await();
            }

//            System.out.println(person.introduceYourself() + " start consuming " + request);

            buffer -= request;
//            System.out.println(person.introduceYourself() + " consumed " + request);

//            printBufferState();

            this.producerCondition.signal();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.lock.unlock();
        }
    }

    public void produce(Producer person, int request){
        try {
            this.lock.lock();

            while ((buffer + request) > this.maxBuffer) {
                this.producerCondition.await();
            }

//            System.out.println(person.introduceYourself() + " start producing " + request);


            buffer += request;
//            System.out.println(person.introduceYourself() + " produced " + request);

//            printBufferState();

            this.consumerCondition.signal();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            this.lock.unlock();
        }
    }


    @Override
    public void consume(Person person) {

    }

    @Override
    public void produce(Person person) {

    }
}
