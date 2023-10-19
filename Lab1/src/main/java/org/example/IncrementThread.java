package org.example;

public class IncrementThread extends Thread{

    public MyInt operator;
    public int numberOfOperation;

    public IncrementThread(MyInt operator, int numberOfOperation){
        this.operator = operator;
        this.numberOfOperation = numberOfOperation;
    }

    @Override
    public void run() {
        System.out.println("Incrementing...");
        for(int i = 0; i < this.numberOfOperation; i++) {
            operator.increment();
        }
    }
}
