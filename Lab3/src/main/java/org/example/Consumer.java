package org.example;


import java.util.Random;

public class Consumer extends Thread implements Person {

    private IBuffer buffer1;
    private int id;
    private final Random random = new Random();
    private int noConsumed;

    private final int maxRequest = 10;

    public Consumer(int id, IBuffer buffer1){
        this.id = id;
        this.buffer1 = buffer1;
    }

    @Override
    public void run() {

        while(true) {
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

//        return this.random.nextInt(maxRequest) + 1;
        if (this.id == 3) return maxRequest;

        return maxRequest;

    }

    public int getNoConsumed(){
        return noConsumed;
    }


}
