package org.example;

import org.example.bin.TimeStamp;

import java.util.ArrayList;

public interface IBuffer {
    public void consume(Person person);
    public void produce(Person person);

    public void consume(Consumer person, int request);
    public void produce(Producer person, int request);

    public default void consume(Person person, int request){

    }
    public default void produce(Person person, int request){

    }
    public void updateHandledRequest(long time);

    public default ArrayList<TimeStamp> getHandledRequestArray(){
        return new ArrayList<TimeStamp>();
    }

    public default void resetHandledRequest(){

    }
    public String toString();

}
