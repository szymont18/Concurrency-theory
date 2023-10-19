package org.example;

public class MyThread extends Thread{

    public int id;

    public MyThread(int id) {
        this.id = id;
    }

    public void run(){
        System.out.println("I am " + this.id + " thread");
    }

}
