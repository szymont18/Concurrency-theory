package org.example;

import org.jcsp.lang.Any2AnyChannelInt;
import org.jcsp.lang.Any2OneChannelInt;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Consumer implements CSProcess {
    private final One2OneChannelInt channel;
//    private final Any2AnyChannelInt channel2;
    public Consumer (One2OneChannelInt in) {
        channel = in;
//        channel2 = null;
    }

//    public Consumer(Any2AnyChannelInt in){
//        channel2 = in;
//        channel = null;
//    }

    public void run () {
        int item;
        item = channel.in().read();
//        else item = channel2.in().read();
        System.out.println("Consuming " + item);

    }
}
