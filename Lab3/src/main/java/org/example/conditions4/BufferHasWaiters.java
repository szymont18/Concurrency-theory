package org.example.conditions4;

import org.example.Consumer;
import org.example.IBuffer;
import org.example.Person;
import org.example.Producer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementation of Producer/Consumer with 4 conditions and hasWaiters()
 */
public class BufferHasWaiters implements IBuffer {
    private int buffer;
    private final int maxBuffer;

    private final ReentrantLock lock;
    private final Condition otherConsumerCondition;
    private final Condition firstConsumerCondition;
    private final Condition otherProducerCondition;
    private final Condition firstProducerCondition;


    public BufferHasWaiters(int maxBuffer){
        this.maxBuffer = maxBuffer;
        this.lock = new ReentrantLock();

        this.otherConsumerCondition = lock.newCondition();
        this.firstConsumerCondition = lock.newCondition();
        this.otherProducerCondition = lock.newCondition();
        this.firstProducerCondition = lock.newCondition();

    }

    public void printBufferState(){
        System.out.println("Buffer state = " + buffer);
        System.out.println();
    }

    @Override
    public void consume(Consumer person, int request) {
        try{
            this.lock.lock();

//            System.out.println(person.introduceYourself() + " request "+ request);

            while(this.lock.hasWaiters(this.firstConsumerCondition)){
//                System.out.println(person.introduceYourself() + " sleep in others");
                this.otherConsumerCondition.await();
//                System.out.println(person.introduceYourself() + " wake up in others while");
            }


            while (buffer < request){
//                System.out.println(person.introduceYourself() + " sleep in first");
                this.firstConsumerCondition.await();
//                System.out.println(person.introduceYourself() + " wakes up as first");
            }


            buffer -= request;
            Thread.sleep(0L, 500);

//            System.out.println(person.introduceYourself() + " consumed " + request);


            this.otherConsumerCondition.signal();
            this.firstProducerCondition.signal();
//            this.printBufferState();


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

//            System.out.println(person.introduceYourself() + " request "+ request);


            while (this.lock.hasWaiters(this.firstProducerCondition)){ // There is waiting Producer that should be served first
//                System.out.println(person.introduceYourself() + " sleep in others");
                this.otherProducerCondition.await();
//                System.out.println(person.introduceYourself() + " wake up in others while");

            }


            while(buffer + request > maxBuffer){
//                System.out.println(person.introduceYourself() + " sleep in first");
                this.firstProducerCondition.await();
//                System.out.println(person.introduceYourself() + " wakes up as first");
            }


            buffer += request;
            Thread.sleep(0L, 500);

//            System.out.println(person.introduceYourself() + " produced " + request);

            this.otherProducerCondition.signal();
            this.firstConsumerCondition.signal();

//            this.printBufferState();


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

