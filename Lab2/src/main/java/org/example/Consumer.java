package org.example;

import org.example.variant1.Buffer1;

public class Consumer extends Thread implements Person {

    private IBuffer buffer1;
    private int id;

    public Consumer(int id, IBuffer buffer1){
        this.id = id;
        this.buffer1 = buffer1;
    }

    @Override
    public void run() {

        while(true) {
            buffer1.consume(this);
        }

    }
    @Override
    public String introduceYourself() {
        return "Consumer " + id;
    }
}
