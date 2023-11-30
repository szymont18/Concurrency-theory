package org.example;




import java.util.ArrayList;
import java.util.Random;

public class Producer extends Thread implements Person {
    private int id;
    private IBuffer buffer1;
    private long threadID;


    private final Random random = new Random(Main.seed);
    private final int maxRequest = 50;

    private long noProduced;

    private boolean running;

    private Integer request = null;


    public Producer(int id, IBuffer buffer1){
        this.id = id;
        this.buffer1 = buffer1;

        this.threadID = this.getId();
        this.noProduced = 0L;

    }

    public Integer getRequest() {
        return request;
    }

    public Producer(int id, IBuffer buffer1, int request){
        this.id = id;
        this.buffer1 = buffer1;

        this.threadID = this.getId();
        this.request = request;
        this.noProduced = 0;

    }

    @Override
    public void run() {
        running=true;
        while(running){
            buffer1.produce(this, getRandomInt());
            noProduced++;
//            try {
//                Thread.sleep(0, 1);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }
    }

    public long getNoProduced() {
        return noProduced;
    }

    @Override
    public String introduceYourself() {
        return "Producer " + id;
    }

    @Override
    public int getRandomInt() {
        if (request != null) return request;

        return this.random.nextInt(maxRequest) + 1;

    }


    public void stopRunning(){
        running=false;
    }


    public long getThreadID() {
        return this.threadID;
    }
}
