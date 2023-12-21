package org.example;

import org.jcsp.lang.*;

import java.util.Random;

public class Consumer implements CSProcess {
    static int maxRequest;

    private final One2OneChannelInt[] buffers;
    private int eatenElements;
    private int jumps;

    public Consumer (One2OneChannelInt[] buffers) {
        this.buffers = buffers;
        eatenElements = 0;
        jumps = 0;

    }
//    private Guard[] fillGuards(One2OneChannelInt[] buffers){
//        Guard[] guards = new Guard[buffers.length];
//        int i = 0;
//        for(One2OneChannelInt buffer : buffers){
//            guards[i] = (Guard) buffer;
//            i++;
//        }
//        return guards;
//    }
    private void introduce(){
        System.out.println("Consumer eats " + this.eatenElements);
    }
    public void run () {
//        System.out.println("Consumer start working");

        Random random = new Random();
        int index, request;

        while(true) {
            // Select Bufer
            index = random.nextInt(this.buffers.length);
            request = random.nextInt(Consumer.maxRequest) + 1;

            // Send request for buffer
            this.buffers[index].out().write(1);


            // Buffer does not send anything
            while (this.buffers[index].in().read() == 0){
                // Resend request
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                jumps++;
                index = random.nextInt(this.buffers.length);
                this.buffers[index].out().write(request);

            }

            this.buffers[index].out().write(request); // Send how much do you want
            int item = this.buffers[index].in().read(); // Get what you want

            eatenElements++;

        }
    }

    public int getJumps() {
        return jumps;
    }

    public int getEatenElements() {
        return eatenElements;
    }
}
