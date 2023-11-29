package org.example;



import java.util.ArrayList;

public interface IBuffer {
    public default void consume(Person person){}
    public default void produce(Person person){}

    public void consume(Consumer person, int request);
    public void produce(Producer person, int request);

    public default void consume(Person person, int request){

    }
    public default void produce(Person person, int request){

    }

    public String toString();

}
