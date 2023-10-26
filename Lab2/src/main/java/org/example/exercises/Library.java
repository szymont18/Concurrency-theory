package org.example.exercises;

import org.example.Person;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Library{

    private int writersNumber;
    private int readersNumber;

    private int writersWaitingNumber;
    private int readersWaitingNumber;
    private Condition writers;
    private Condition readers;
    private Lock lock;

    public Library(){
        writersNumber = 0;
        readersNumber = 0;
        writersWaitingNumber = 0;
        readersWaitingNumber = 0;

        lock = new ReentrantLock();
        writers = lock.newCondition();
        readers = lock.newCondition();
    }

    public void startReading(Person person){

        try {
            this.lock.lock();

            readersWaitingNumber++;
            if (writersWaitingNumber > 0 || writersNumber > 0) {
                readers.await();
            }
            readersNumber++;
            readersWaitingNumber--;

            System.out.println(person.introduceYourself() + " started");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            this.lock.unlock();
        }
    }


    public void endReading(Person person){
        try {
            this.lock.lock();
            System.out.println(person.introduceYourself() + " ended ");
            readersNumber--;

            if(readersNumber == 0){
                writers.signal();
            }
        }
        finally {
            this.lock.unlock();
        }
    }

    public void startWriting(Person person){

        try {
            this.lock.lock();

            writersWaitingNumber++;
            while (readersNumber + writersNumber > 0){
                writers.await();
            }
            writersWaitingNumber--;
            writersNumber = 1;

            System.out.println(person.introduceYourself() + " started ");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void endWriting(Person person){
        try{

            writersNumber = 0;
            System.out.println(person.introduceYourself() + " ended");

            if (readersWaitingNumber == 0){
                writers.signal();
            }
            else{
                readers.signalAll();
            }

        }
        finally {
            this.lock.unlock();
        }
    }

}
