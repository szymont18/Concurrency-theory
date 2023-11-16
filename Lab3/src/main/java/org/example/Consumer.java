package org.example;


import java.util.Random;

public class Consumer extends Thread implements Person {

    private IBuffer buffer1;
    private int id;
    private final Random random = new Random();
    private int noConsumed;

    private final int maxRequest = 25;

    public Consumer(int id, IBuffer buffer1){
        this.id = id;
        this.buffer1 = buffer1;
    }

    @Override
    public void run() {

        while(true) {
            buffer1.consume(this, getRandomInt());
            noConsumed++;
//            System.out.println("Consumer " + id + " consumed " + noConsumed + " times");
        }

    }
    @Override
    public String introduceYourself() {
        return "Consumer " + id;
    }

    @Override
    public int getRandomInt() {

        if (this.id == 1) return maxRequest;

        return  1;

    }

    public int getNoConsumed(){
        return noConsumed;
    }


}
