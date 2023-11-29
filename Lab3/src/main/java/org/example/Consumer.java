package org.example;


import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Random;

public class Consumer extends Thread implements Person {

    private IBuffer buffer1;
    private int id;
    private long threadID;
    private final Random random = new Random(Main.seed);
    private long noConsumed;



    private final int maxRequest = 50;
    private boolean running;
    private Integer request = null;

    public Consumer(int id, IBuffer buffer1){
        this.id = id;
        this.buffer1 = buffer1;

        this.threadID = this.getId();
        this.noConsumed = 0;
    }
    public Consumer(int id, IBuffer buffer1, int request){
        this.id = id;
        this.buffer1 = buffer1;

        this.threadID = this.getId();
        this.request = request;
        this.noConsumed = 0L;
    }

    @Override
    public void run() {
        running = true;

        while(running) {
            buffer1.consume(this, getRandomInt());
            noConsumed++;
//            try {
//                Thread.sleep(0, 10);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

        }

    }
    @Override
    public String introduceYourself() {
        return "Consumer " + id;
    }

    @Override
    public int getRandomInt() {
        if(request != null) return request;

        return this.random.nextInt(maxRequest) + 1;

    }

    public long getNoConsumed(){
        return noConsumed;
    }

//    public void updateTime(long time){
//        timeStamps.add(new TimeStamp((float) time / 1000000000, this.noConsumed));
//    }

    public void stopRunning(){
        running=false;
    }


    public Integer getRequest() {
        return request;
    }

    public long getThreadID() {
        return this.threadID;
    }
}
