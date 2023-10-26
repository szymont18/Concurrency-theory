package org.example.exercises;

import org.example.Person;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Philosopher extends Thread implements Person {

    private final int id;
    private final Forks forks;
    private final Random random = new Random();

    public Philosopher(int id, Forks forks) {
        this.id = id;
        this.forks = forks;
    }

    @Override
    public String introduceYourself() {
        return "Philosopher " + id;
    }

    private void sleep(){
        try {
            int randomSleepTime = random.nextInt(5) + 1;
            long sleepTimeInSeconds = TimeUnit.SECONDS.toMillis(randomSleepTime);
            Thread.sleep(sleepTimeInSeconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Wątek został przerwany podczas snu.");
        }
    }

    public int getArrayID(){
        return this.id - 1;
    }

    @Override
    public void run(){
        while (true){
            this.forks.getFork(this);
//            sleep();
            this.forks.giveFork(this);
        }


    }
}
