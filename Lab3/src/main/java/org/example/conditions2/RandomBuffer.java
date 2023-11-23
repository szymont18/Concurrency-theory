package org.example.conditions2;

import org.example.Consumer;
import org.example.IBuffer;
import org.example.Person;
import org.example.Producer;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Multi element buffer with random number of request from one person - 2 Conditions

 * Deadlock (Inappropriate buffer length)
 * max_buffer = 5
 1) P1, C1
 buffer_state = 0
 consumerCondition = {}
 producerCondition = {}

 2) P1
 buffer_state = 0
 consumerCondtion = {C1(wait for 4)}
 producerCondition = {}

 3) P1
 buffer_state = 3
 consumerCondtion = {C1(wait for 4)}
 producerCondition = {}

 4)
 buffer_state = 3
 consumerCondtion = {C1(wait for 4)}
 producerCondition = {P1(produced 3)}

 DEADLOCK !!!

 * Starvation
 * max_buffer = 7

 1) P1, C1, C2
 buffer_state = 0
 consumerCondtion = {}
 producerCondition = {}

 2) P1, C1
 buffer_state = 0
 consumerCondtion = {C2(wait for 3)}
 producerCondition = {}

 3) P1
 buffer_state = 0
 consumerCondtion = {C2(wait for 3), C1(wait for 1)}
 producerCondition = {}

 4) P1, C2(wait for 3)
 buffer_state = 1
 consumerCondtion = {, C1(wait for 1)}
 producerCondition = {}

 5) P1,
 buffer_state = 1
 consumerCondtion = {C2(wait for 3), C1(wait for 1)}
 producerCondition = {}

 6) P1,  C1(wait for 1)
 buffer_state = 2
 consumerCondtion = {C2(wait for 3),}
 producerCondition = {}

 7) P1,  C1
 buffer_state = 1
 consumerCondtion = {C2(wait for 3)}
 producerCondition = {}

 8) P1,  C1
 buffer_state = 0
 consumerCondtion = {C2(wait for 3)}
 producerCondition = {}

 AGAIN 2 - 8; AGAIN 2- 8 ...
 C2 is starving
 Process with more-element-request starves more often
 */
public class RandomBuffer implements IBuffer {
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
            Thread.sleep(0L, 500);

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
            Thread.sleep(0L, 500);

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
    public void updateHandledRequest(long time) {

    }


    @Override
    public void consume(Person person) {

    }

    @Override
    public void produce(Person person) {

    }
}
