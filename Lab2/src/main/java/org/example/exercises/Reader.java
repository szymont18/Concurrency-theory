package org.example.exercises;

import org.example.Person;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Reader extends Thread implements Person {

    private final int id;
    private final Library library;

    private final Random random = new Random();

    public Reader(int id, Library library){
        this.id = id;
        this.library = library;
    }

    @Override
    public String introduceYourself() {
        return "Reader " + id;
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

    @Override
    public void run(){
        while(true) {
            this.library.startReading(this);
            this.sleep();
            this.library.endReading(this);
        }
    }
}
