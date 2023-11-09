package org.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StarvationFreeBuffer implements IBuffer{
    private int buffer;
    private final int maxBuffer;

    private final Lock lock;
    private final Condition consumerCondition;
    private final Condition firstConsumerCondition;
    private final Condition producerCondition;
    private final Condition firstProducerCondition;

    boolean waitingProducer;
    boolean waitingConsumer;

    public StarvationFreeBuffer(int maxBuffer){
        this.maxBuffer = maxBuffer;
        this.lock = new ReentrantLock();

        this.consumerCondition = lock.newCondition();
        this.firstConsumerCondition = lock.newCondition();
        this.producerCondition = lock.newCondition();
        this.firstProducerCondition = lock.newCondition();

        this.waitingConsumer = false;
        this.waitingProducer = false;
    }

    @Override
    public void consume(Consumer person, int request) {
        try{
            this.lock.lock();


            while(this.waitingConsumer){
                this.consumerCondition.await();
            }

            this.waitingConsumer = true;

            while (buffer - request < 0){
                this.firstConsumerCondition.await();
            }

            this.waitingConsumer = false;

            buffer -= request;

            this.consumerCondition.signal();
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
                this.producerCondition.await();
            }

            this.waitingProducer = true;

            while(buffer + request > maxBuffer){

                this.firstProducerCondition.await();
            }

            this.waitingProducer = false; // Stop waiting

            buffer += request;

            this.producerCondition.signal();
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
