package org.example.conditions4;

import org.example.Consumer;
import org.example.IBuffer;
import org.example.Person;
import org.example.Producer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementation of Producer/Consumer with 4 conditions and hasWaiters()
 */
public class BufferHasWaiters implements IBuffer {
    private int buffer;
    private final int maxBuffer;

    private final ReentrantLock lock;
    private final Condition otherConsumerCondition;
    private final Condition firstConsumerCondition;
    private final Condition otherProducerCondition;
    private final Condition firstProducerCondition;


    public BufferHasWaiters(int maxBuffer){
        this.maxBuffer = maxBuffer;
        this.lock = new ReentrantLock();

        this.otherConsumerCondition = lock.newCondition();
        this.firstConsumerCondition = lock.newCondition();
        this.otherProducerCondition = lock.newCondition();
        this.firstProducerCondition = lock.newCondition();

    }

    public void printBufferState(){
        System.out.println("Buffer state = " + buffer);
        System.out.println();
    }

    @Override
    public void consume(Consumer person, int request) {
        try{
            this.lock.lock();


            while(this.lock.hasWaiters(this.firstConsumerCondition)){
                this.otherConsumerCondition.await();
            }


            while (buffer < request){
                this.firstConsumerCondition.await();
            }


            buffer -= request;
            Thread.sleep(0L, 500);

//            System.out.println(person.introduceYourself() + " consumed " + request);


            this.otherConsumerCondition.signal();
            this.firstProducerCondition.signal();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void produce(Producer person, int request) {
        try {
            this.lock.lock();

            while (this.lock.hasWaiters(this.firstProducerCondition)){ // There is waiting Producer that should be served first
                this.otherProducerCondition.await();
            }


            while(buffer + request > maxBuffer){
                this.firstProducerCondition.await();
            }


            buffer += request;
            Thread.sleep(0L, 500);

//            System.out.println(person.introduceYourself() + " produced " + request);

            this.otherProducerCondition.signal();
            this.firstConsumerCondition.signal();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.lock.unlock();
        }


    }

    @Override
    public void consume(Person person) {

    }

    @Override
    public void produce(Person person) {

    }
}
/*
buffer_state = 9
firstP = {P4(2), P1(5)}
otherP = {P2, P3}


Starvation on 4 conditions with hasWaiters()
max_buff = 10

1) P1, P2, P3 C1
buffer_state = 0
firstP = {}
otherP = {}

* P1 produced 5

2) P1, P2, P3, C1
buffer_state = 5
firstP = {}
otherP = {}

* P1 produced 3

3) P1, P2, P3, C1
buffer_state = 8
firstP = {}
otherP = {}

* P1 wants to produce 5
3) P2, P3, C1
buffer_state = 8
firstP = {P1(5)}
otherP = {}

* C1 consume 1 and signal first Producer
4) P2, P3, C1
buffer_state = 7
firstP = {P1(5)}
otherP = {}

* P1 wake's up but did not get the lock
5) P2, P3, C1, #P1(5)
buffer_state = 7
firstP = {}
otherP = {}

* P2 wants to produce 4
6) P2, P3, C1, #P1(5)
buffer_state = 7
firstP = {P2(4)}
otherP = {}

* P1 gets the lock, but still can not produce due to buffer size
7) P3, C1
buffer_state = 7
firstP = {P2(4), P1(5)}
otherP = {}

* P3 wants to produce 3
8) C1
buffer_state = 7
firstP = {P2(4), P1(5)}
otherP = {P3(3)}

* C1 consumed 1 and wake up P2
9) C1, #P2(4)
buffer_state = 6
firstP = {P1(5)}
otherP = {P3(3)}

* P2 produced 4
10) C1, P2
buffer_state = 10
firstP = {P1(5)}
otherP = {P3(3)}

# Now we can repeat 5 - 10 steps. It shows that P1 is starving.


Deadlock 4 conditions
max_buff = 10

1) P1, P2, P3 C1, C2
buffer_state = 0
firstP = {}
otherP = {}

* P1 produced 5

2) P1, P2, P3, C1, C2
buffer_state = 5
firstP = {}
otherP = {}

* P1 produced 5

3) P1, P2, P3, C1, C2
buffer_state = 10
firstP = {}
otherP = {}

* P1 wants to produce 5
3) P2, P3, C1, C2
buffer_state = 10
firstP = {P1(5)}
otherP = {}

* C1 consume 1 and signal first Producer
4) P2, P3, C1, C2,#P1(5)
buffer_state = 9
firstP = {}
otherP = {}

* P2 wants to produce 2
5) P2, P3, C1, C2, #P1(5)
buffer_state = 9
firstP = {P2(2)}
otherP = {}

* P1 gets the lock, but still can not produce due to buffer size
7) P3, C1, C2
buffer_state = 9
firstP = {P2(2), P1(5)}
otherP = {}

* C1 consume 3 and signal P1(5)
7) P3, C1, C2, #P1(5)
buffer_state = 6
firstP = {P2(2)}
otherP = {}

* P1 gets the lock
8) P3, C1, C2
buffer_state = 6
firstP = {P2(2), P1(5)}
otherP = {}

* C1 consume 5 and signal P2
8) P3, C1, C2, #P2(2)
buffer_state = 1
firstP = {P1(5)}
otherP = {}

* C1 wants to consume 5
9)P3, C2, #P2(2)
buffer_state = 1
firstP = {P1(5)}
otherP = {}
firstC = {C1(5)}
otherC = {}

* P2 get locks and produces 2 and signal C1
9)P3, C2, P2, #C1(5)
buffer_state = 3
firstP = {P1(5)}
otherP = {}
firstC = {}
otherC = {}

* C2 want to consume 5
10)P3, P2, #C1(5)
buffer_state = 3
firstP = {P1(5)}
otherP = {}
firstC = {C2(5)}
otherC = {}

* C1 gets the lock but still can not consume
11)P3, P2
buffer_state = 3
firstP = {P1(5)}
otherP = {}
firstC = {C1(5), C2(5)}
otherC = {}

* P2 enters and wait on other
P3,
12) buffer_state = 3
firstP = {P1(5)}
otherP = {P2}
firstC = {C1(5), C2(5)}
otherC = {}

* P3 get the lock and wait on other
13) buffer_state = 3
firstP = {P1(5)}
otherP = {P2}
firstC = {C1(5), C2(5)}
otherC = {}











*/