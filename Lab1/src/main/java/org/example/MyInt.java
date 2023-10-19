package org.example;

public class MyInt{
    public int number;

    public MyInt(int number){
        this.number = number;
    }

    public void increment(){
        this.number++;
    }

    public void decrement(){
        this.number--;
    }

    public void printNumber(){
        System.out.println("Actual number is " + this.number);
    }

}
