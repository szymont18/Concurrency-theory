package org.example;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        Example 1: Create ten Threads
//        System.out.println("Start");
//
//        for(int i = 0; i < 10; i++){
//            MyThread thread = new MyThread(i);
//            thread.start();
//        }
//        System.out.println("End");

        // Example 2: Create threads which decrements or increments int
    /*
         Observations:
         operations : threads
         100:1 = 0
         10000:1 = -658
         1000:1 = -102, 0, 24
         100: 10 = -5, 494
         10: 1000 = 0, 0, 0
         100: 3 = 0
        100: 100 = 0
        To receive 0 there is better to create more Threads. If you want to get non-zero number there is
        better to set more ThreadsOperation.

        Łatwiej jest zepsuć przez ilość operacji w pętli niż przez wątki ze względu na architekturę komputera
        */
        int incrementsThreadsOperation = 100;
        int decrementsThreadsOperation = 100;
        int numberOfThreads = 100;

        MyInt myint = new MyInt(0);

        for(int i = 0; i < numberOfThreads; i++){
            DecrementThread decrementThread = new DecrementThread(myint,decrementsThreadsOperation);
            IncrementThread incrementThread = new IncrementThread(myint, incrementsThreadsOperation);

            decrementThread.start();
            incrementThread.start();

            decrementThread.join();
            incrementThread.join();
        }


        myint.printNumber();


    }
}