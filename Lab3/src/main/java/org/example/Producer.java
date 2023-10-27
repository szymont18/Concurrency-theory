package org.example;


import java.util.Random;

public class Producer extends Thread implements Person {
    private int id;
    private IBuffer buffer1;

    private final Random random = new Random();
    private final int maxRequest = 10;

    public Producer(int id, IBuffer buffer1){
        this.id = id;
        this.buffer1 = buffer1;
    }

    @Override
    public void run() {
        while(true){
            buffer1.produce(this, getRandomInt());
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
}
