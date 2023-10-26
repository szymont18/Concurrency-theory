package org.example.exercises;

import org.example.IBuffer;
import org.example.Person;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RandomBuffer implements IBuffer {

    private int buffer;
    private final int maxSize;

    private final Lock lock;
    private final Condition firstConsumer;
    private final Condition otherConsumers;
    private final Condition firstProducer;
    private final Condition otherProducers;

    private int waitingProducersNumber;
    private int waitingConsumersNumber;


    private final Random random;

    public RandomBuffer(int maxSize){
        this.buffer = 0;
        this.maxSize = maxSize;

        this.waitingConsumersNumber = 0;
        this.waitingProducersNumber = 0;

        this.lock = new ReentrantLock();
        this.firstConsumer = this.lock.newCondition();
        this.otherConsumers = this.lock.newCondition();

        this.firstProducer = this.lock.newCondition();
        this.otherProducers = this.lock.newCondition();

        this.random = new Random();
    }


    private int getRandomInt(){
        return this.random.nextInt(maxSize / 2) + 1;
    }

    public void printBufferState(){
        System.out.println("Buffer state = " + buffer);
    }

    @Override
    public void consume(Person person) {
        int consumeNumber = this.getRandomInt();

        try {
            this.lock.lock();

            this.waitingConsumersNumber++;

            if (this.waitingConsumersNumber > 1){
                this.otherConsumers.await();
            }

            while (consumeNumber > buffer) {
                this.firstConsumer.await();
            }

            this.buffer -= consumeNumber;
            System.out.println(person.introduceYourself() + " consumed: " + consumeNumber);
            this.printBufferState();

            this.waitingConsumersNumber--;

            this.otherConsumers.signal();
            this.firstProducer.signal();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void produce(Person person) {
        int produceNumber = this.getRandomInt();

        try{
            this.lock.lock();

            this.waitingProducersNumber++;

            if(this.waitingProducersNumber > 2){
                this.otherProducers.await();

            }

            while(this.buffer + produceNumber > this.maxSize){
                this.firstProducer.await();
            }

            this.buffer += produceNumber;
            System.out.println(person.introduceYourself() + " produced: " + produceNumber);
            this.printBufferState();
            this.waitingProducersNumber--;

            this.firstConsumer.signal();
            this.otherProducers.signal();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            this.lock.unlock();
        }

    }
}
