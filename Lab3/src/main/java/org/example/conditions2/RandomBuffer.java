package org.example.conditions2;

import org.example.Consumer;
import org.example.IBuffer;
import org.example.Person;
import org.example.Producer;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
2 Conditions
 */
public class RandomBuffer implements IBuffer {
    private int buffer;
    private final int maxBuffer;

    private final ReentrantLock lock;
    private final Condition consumerCondition;
    private final Condition producerCondition;

    private int handledRequest;


    public RandomBuffer(int maxBuffer){
        this.buffer = 0;
        this.maxBuffer = maxBuffer;

        this.lock = new ReentrantLock();
        this.consumerCondition = lock.newCondition();
        this.producerCondition = lock.newCondition();


        handledRequest = 0;
    }


    public void printBufferState(){
        System.out.println("Buffer state = " + buffer);
        System.out.println();
    }



    public void consume(Consumer person, int request){
        try {
            this.lock.lock();

            while (buffer < request) {
                this.consumerCondition.await();
            }


            buffer -= request;

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

            while (buffer + request > maxBuffer) {
                this.producerCondition.await();
            }


            buffer += request;


            this.consumerCondition.signal();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            this.lock.unlock();
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
        return "TwoConditions";
    }
}
