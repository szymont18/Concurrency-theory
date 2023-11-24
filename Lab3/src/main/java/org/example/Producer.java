package org.example;


import org.example.time.TimeStamp;

import java.util.ArrayList;
import java.util.Random;

public class Producer extends Thread implements Person {
    private int id;
    private IBuffer buffer1;
    private long threadID;


    private final Random random = new Random(Main.seed);
    private final int maxRequest = 25;

    private int noProduced;
    private ArrayList<TimeStamp> timeStamps;
    private boolean running;



    public Producer(int id, IBuffer buffer1){
        this.id = id;
        this.buffer1 = buffer1;
        timeStamps = new ArrayList<>();
        this.threadID = this.getId();

    }

    @Override
    public void run() {
        running=true;
        while(running){
            buffer1.produce(this, getRandomInt());
            noProduced++;
        }
    }

    @Override
    public String introduceYourself() {
        return "Producer " + id;
    }

    @Override
    public int getRandomInt() {

        return this.random.nextInt(maxRequest) + 1;

    }

    public void updateTime(long time){
        timeStamps.add(new TimeStamp(time, this.noProduced));
    }

    public void stopRunning(){
        running=false;
    }


    public long getThreadID() {
        return this.threadID;
    }
}
