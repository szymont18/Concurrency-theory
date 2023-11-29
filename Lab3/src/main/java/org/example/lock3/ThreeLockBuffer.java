package org.example.lock3;

import org.example.Consumer;
import org.example.IBuffer;
import org.example.Person;
import org.example.Producer;


import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreeLockBuffer implements IBuffer {
    private int buffer;
    private final int maxBuffer;

    private final ReentrantLock commonLock;
    private final ReentrantLock producerLock;
    private final ReentrantLock consumerLock;

    private final Condition waiting;

    private int handledRequest;



    public ThreeLockBuffer(int maxBuffer){
        this.maxBuffer = maxBuffer;
        this.consumerLock = new ReentrantLock();
        this.commonLock = new ReentrantLock();
        this.producerLock = new ReentrantLock();

        this.waiting = this.commonLock.newCondition();

        handledRequest = 0;
    }

    public void printBufferState(){
        System.out.println("Buffer state = " + buffer);
        System.out.println();
    }

    @Override
    public void consume(Consumer person, int request) {
        try{
            this.consumerLock.lock();

            this.commonLock.lock();


            while (buffer < request){
                this.waiting.await();
            }

            buffer -= request;

            this.waiting.signal();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.commonLock.unlock();
            this.consumerLock.unlock();
        }
    }

    @Override
    public void produce(Producer person, int request) {
        try {
            this.producerLock.lock();

            this.commonLock.lock();

            while(buffer + request > maxBuffer){

                this.waiting.await();
            }

            buffer += request;


            this.waiting.signal();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {

            this.commonLock.unlock();
            this.producerLock.unlock();
        }

    }



    public void resetHandledRequest(){
        handledRequest = 0;

    }

    @Override
    public void consume(Person person) {

    }

    @Override
    public void produce(Person person) {

    }

    @Override
    public String toString() {
        return "ThreeLock";
    }
}


