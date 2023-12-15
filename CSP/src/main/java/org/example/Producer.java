package org.example;

import org.jcsp.lang.Any2AnyChannelInt;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Producer implements CSProcess {


    final private One2OneChannelInt channel;
//    final private Any2AnyChannelInt channel2;

    public Producer(One2OneChannelInt channel) {
        this.channel = channel;
//        channel2 = null;
    }

//    public Producer(Any2AnyChannelInt channel) {
//        this.channel = null;
//        this.channel2 = channel;
//    }

    public void run(){
        int item = (int) (Math.random() * 100) + 1;
        System.out.println("Sending " + item);
        if (channel != null) channel.out().write(item);
//        else channel2.out().write(item);
    }

}
