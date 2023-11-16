package org.example.conditions4;

import org.example.Consumer;
import org.example.IBuffer;
import org.example.Person;
import org.example.Producer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Spróbuj robić sleepami + implementacja z hasWaiters()

/**
 * Implementation of 4 conditions
 */
public class StarvationFreeBuffer implements IBuffer {
    private int buffer;
    private final int maxBuffer;

    private final ReentrantLock lock;
    private final Condition otherConsumerCondition;
    private final Condition firstConsumerCondition;
    private final Condition otherProducerCondition;
    private final Condition firstProducerCondition;

    boolean waitingProducer;
    boolean waitingConsumer;

    public StarvationFreeBuffer(int maxBuffer){
        this.maxBuffer = maxBuffer;
        this.lock = new ReentrantLock();

        this.otherConsumerCondition = lock.newCondition();
        this.firstConsumerCondition = lock.newCondition();
        this.otherProducerCondition = lock.newCondition();
        this.firstProducerCondition = lock.newCondition();

        this.waitingConsumer = false;
        this.waitingProducer = false;
    }

    public void printBufferState(){
        System.out.println("Buffer state = " + buffer);
        System.out.println();
    }

    @Override
    public void consume(Consumer person, int request) {
        try{
            this.lock.lock();


            while(this.waitingConsumer){
                this.otherConsumerCondition.await();
            }

            this.waitingConsumer = true;

            while (buffer < request){
                this.firstConsumerCondition.await();
            }

            this.waitingConsumer = false;

            buffer -= request;
            Thread.sleep(0L, 500);

//            System.out.println(person.introduceYourself() + " consumed " + request);


            this.otherConsumerCondition.signal();
            this.firstProducerCondition.signal();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void produce(Producer person, int request) {
        try {
            this.lock.lock();

            while (this.waitingProducer){ // There is waiting Producer that should be served first
                this.otherProducerCondition.await();
            }

            this.waitingProducer = true;

            while(buffer + request > maxBuffer){

                this.firstProducerCondition.await();
            }

            this.waitingProducer = false; // Stop waiting

            buffer += request;
            Thread.sleep(0L, 500);

//            System.out.println(person.introduceYourself() + " produced " + request);

            this.otherProducerCondition.signal();
            this.firstConsumerCondition.signal();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
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
