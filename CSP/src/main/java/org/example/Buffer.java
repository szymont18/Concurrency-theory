package org.example;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

public class Buffer implements CSProcess {
    static int maxBuffer;


    final private One2OneChannelInt[] producers;
    final private One2OneChannelInt[] consumers;
    private int buffer;
    private BufferState state;
    private int reservedProducer;
    private int reservedConsumer;

    private int counter;


    public Buffer(One2OneChannelInt[] producers, One2OneChannelInt[] consumers) {
        this.producers = producers;
        this.consumers = consumers;

        this.buffer = 0;
        this.state = BufferState.BUY;
        this.reservedProducer = -1;
        this.reservedConsumer = -1;
        this.counter = 0;
    }

    private Guard[] fillGuards(One2OneChannelInt[] producers, One2OneChannelInt[] consumers){
        Guard[] guards = new Guard[this.producers.length + this.consumers.length];
        int i = 0;
        for (One2OneChannelInt producer : producers){
            guards[i] = producer.in();
            i++;
        }

        for (One2OneChannelInt consumer: consumers){
            guards[i] = consumer.in();
            i++;
        }
        return guards;
    }

    public void run(){
//        System.out.println("Buffer starts working");
        Guard[] guards = this.fillGuards(this.producers, this.consumers);

        Alternative alt = new Alternative(guards);

        int index;
        int consumersIndex = this.producers.length; // Index of first Consumer
        while(true){
//            System.out.println("Buffer state: " + state + " reserved = " + reservedIndex);
            index = alt.select();

            if(index < consumersIndex){
                this.handleProducer(this.producers, index);
            }

            else{
                this.handleConsumer(this.consumers, index - consumersIndex);
            }
        }
    }

    private void checkChangeBufferStatus(){
        BufferState oldState = this.state;
        if(buffer >= maxBuffer / 2 && state == BufferState.BUY) state = BufferState.SELL;
        if(buffer < maxBuffer / 2 && state == BufferState.SELL) state = BufferState.BUY;
    }

    private void handleProducer(One2OneChannelInt[] producers, int index){
        if(this.state == BufferState.BUY){ // Ready to produce

            if(this.reservedProducer == index){
                this.buffer += producers[index].in().read();

                this.reservedProducer = -1;
            }

            else{
                producers[index].in().read();

                if(reservedProducer == -1) {
                    this.reservedProducer = index;
                    producers[index].out().write(1); // Producer is prior
                }

                else{
                    producers[index].out().write(0); // Producer is rejected
                }
            }

        }
        else{
            producers[index].in().read();
            producers[index].out().write(0);
        }

        checkChangeBufferStatus();

    }

    private void handleConsumer(One2OneChannelInt[] consumers, int index){
        if(this.state == BufferState.SELL){ // Ready to consume

            if(this.reservedConsumer == index){
                int request = consumers[index].in().read();
                this.buffer -= request;
                this.reservedConsumer = -1;
                this.counter++;
                consumers[index].out().write(request);
            }

            else{
                consumers[index].in().read();

                if(reservedConsumer == -1) {
                    this.reservedConsumer = index;
                    consumers[index].out().write(1); // Consumer is prior
                }

                else{
                    consumers[index].out().write(0); // Producer is rejected
                }

            }

        }
        else{
            consumers[index].in().read();
            consumers[index].out().write(0);
        }

        checkChangeBufferStatus();

    }

    public int getCounter() {
        return counter;
    }
}
