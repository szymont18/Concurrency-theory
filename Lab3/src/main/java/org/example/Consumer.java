package org.example;


import org.example.time.TimeStamp;

import java.util.ArrayList;
import java.util.Random;

public class Consumer extends Thread implements Person {

    private IBuffer buffer1;
    private int id;
    private long threadID;
    private final Random random = new Random();
    private int noConsumed;

    private ArrayList<TimeStamp> timeStamps;

    private final int maxRequest = 25;
    private boolean running;

    public Consumer(int id, IBuffer buffer1){
        this.id = id;
        this.buffer1 = buffer1;
        timeStamps = new ArrayList<>();
        this.threadID = this.getId();
    }

    @Override
    public void run() {
        running = true;

        while(running) {
            buffer1.consume(this, getRandomInt());
            noConsumed++;

        }

    }
    @Override
    public String introduceYourself() {
        return "Consumer " + id;
    }

    @Override
    public int getRandomInt() {

        return this.random.nextInt(maxRequest) + 1;

    }

    public int getNoConsumed(){
        return noConsumed;
    }

    public void updateTime(long time){
        timeStamps.add(new TimeStamp((float) time / 1000000000, this.noConsumed));
    }

    public void stopRunning(){
        running=false;
    }

    public ArrayList<TimeStamp> getTimeStamps() {
        return timeStamps;
    }

    public long getThreadID() {
        return this.threadID;
    }
}
