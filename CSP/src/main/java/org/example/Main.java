package org.example;

import org.jcsp.lang.*;

public class Main {
    public static void main(String[] args) {

        // One Consumer, One Producer
        System.out.println("Start system");
        One2OneChannelInt channel = Channel.one2oneInt();

        CSProcess[] process = {new Consumer(channel), new Producer(channel)};

        Parallel program = new Parallel(process);
        program.run();
        /*
        Bufor możę czekać zarówno na Konsumentów (requesty) jak i Producentów ( elementy, które wytworzą ).
        Trzeba zrobić, żeby było więcej Konsumentów i Producentów. Do tego trzeba użyć alternatyw dla Producentów i
        Konsumentów.
         */
        // Many Consumers, Many Producers
//        Any2AnyChannelInt channel = Channel.any2anyInt();
//        CSProcess[] processMany = {new Consumer(channel), new Consumer(channel), new Consumer(channel),
//                                    new Consumer(channel), new Consumer(channel), new Consumer(channel),
//                                    new Producer(channel), new Producer(channel), new Producer(channel),
//                                    new Producer(channel), new Producer(channel), new Producer(channel)};
//        Parallel programMany = new Parallel(processMany);
//        programMany.run();

    }
}