package org.example;

public class DecrementThread extends Thread {
    public MyInt operator;
    public int numberOfOperation;

    public DecrementThread(MyInt operator, int numberOfOperation){
        this.operator = operator;
        this.numberOfOperation = numberOfOperation;
    }

    @Override
    public void run() {
        System.out.println("Decrementing...");
        for(int i = 0; i < this.numberOfOperation; i++) {
            operator.decrement();
        }
    }
}
