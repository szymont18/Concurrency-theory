package org.example.exercises;

import org.example.Person;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Forks {

    private final int [] freeForks;
    private final int noPhilosophers;
    private final Lock lock;

    private final Condition[] philosophersQueue;

    public Forks(int noPhilosophers) {
        this.freeForks = new int[noPhilosophers];

        for(int i = 0; i < noPhilosophers; i++){
            freeForks[i] = 2;
        }

        this.lock = new ReentrantLock();
        this.noPhilosophers = noPhilosophers;

        this.philosophersQueue = new Condition[noPhilosophers];
        for(int i = 0; i < noPhilosophers; i++){
            philosophersQueue[i] = lock.newCondition();
        }

    }


    private int [] getNextAndPrevious(int id){
        int [] nextAndPrevious = new int[2];

        nextAndPrevious[0] = id - 1;

        if (nextAndPrevious[0] < 0){
            nextAndPrevious[0] = noPhilosophers - 1;
        }

        nextAndPrevious[1] = id + 1;
        if (nextAndPrevious[1] == noPhilosophers){
            nextAndPrevious[1] = 0;
        }
        return nextAndPrevious;

    }
    public void getFork(Philosopher philosopher){
        try {
            this.lock.lock();
            int id = philosopher.getArrayID();
            while (freeForks[id] < 2) {
                philosophersQueue[id].await();
            }

            System.out.println(philosopher.introduceYourself() + " starts ");

            int[] neigh = getNextAndPrevious(id);

            freeForks[neigh[0]] -= 1;
            freeForks[neigh[1]] -= 1;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            this.lock.unlock();

        }
    }

    public void giveFork(Philosopher philosopher){
        try{
            this.lock.lock();
            int id = philosopher.getArrayID();

            int [] neigh = getNextAndPrevious(id);

            freeForks[neigh[0]] += 1;
            freeForks[neigh[1]] += 1;
            System.out.println(philosopher.introduceYourself() + " ends");


            if(freeForks[neigh[0]] == 2){
                this.philosophersQueue[neigh[0]].signal();
            }

            if(freeForks[neigh[1]] == 2){
                this.philosophersQueue[neigh[1]].signal();
            }
        }finally {
            this.lock.unlock();
        }
    }

}
