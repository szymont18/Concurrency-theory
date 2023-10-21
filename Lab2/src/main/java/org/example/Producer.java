package org.example;

import org.example.variant1.Buffer1;

public class Producer extends Thread implements Person {
    private int id;
    private IBuffer buffer1;

    public Producer(int id, IBuffer buffer1){
        this.id = id;
        this.buffer1 = buffer1;
    }

    @Override
    public void run() {
        while(true){
            buffer1.produce(this);
        }
    }

    @Override
    public String introduceYourself() {
        return "Producer " + id;
    }
}
