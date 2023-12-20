package org.example;

import org.jcsp.lang.Any2AnyChannelInt;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.Random;

public class Producer implements CSProcess {
    static int maxRequest;

    final private One2OneChannelInt[] buffers;


    public Producer(One2OneChannelInt[] buffers) {
        this.buffers = buffers;

    }

    public void run(){
//        System.out.println("Producer start working");

        Random random = new Random();
        int index, item;
        while(true) {
            // Produce elements
            item = random.nextInt(Producer.maxRequest) + 1;

            // Select Bufer
            index = random.nextInt(this.buffers.length);

            this.buffers[index].out().write(0); // Send Request

            while (this.buffers[index].in().read() == 0){
                index = random.nextInt(this.buffers.length);
                this.buffers[index].out().write(0); // Send Request
            }

            // Request Accepted
            this.buffers[index].out().write(item);

        }

    }

}
