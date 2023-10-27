package org.example;

public interface IBuffer {
    public void consume(Person person);
    public void produce(Person person);

    public void consume(Consumer person, int request);
    public void produce(Producer person, int request);
}
