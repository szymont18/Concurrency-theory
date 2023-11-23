package org.example.time;

import org.example.Consumer;
import org.example.IBuffer;
import org.example.Producer;
import org.example.lock3.ThreeLockBuffer;

import java.beans.Customizer;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class TimeMeter {

    private IBuffer buffer1;
    private IBuffer buffer2;

    private final int noConsumer;
    private final int noProducer;

    private ArrayList<Consumer> consumers1;
    private ArrayList<Consumer> consumers2;
    private ArrayList<Producer> producers1;
    private ArrayList<Producer> producers2;

    private final ThreadMXBean threadMXBean;
    private boolean firstTurn;
    private boolean CPUTime;

    public TimeMeter(IBuffer buffer1, IBuffer buffer2, int noConsumer, int noProducer){
        this.buffer1 = buffer1;
        this.buffer2 = buffer2;

        this.noConsumer = noConsumer;
        this.noProducer = noProducer;

        consumers1 = new ArrayList<>();
        consumers2 = new ArrayList<>();
        producers1 = new ArrayList<>();
        producers2 = new ArrayList<>();

        this.threadMXBean = ManagementFactory.getThreadMXBean();
        this.threadMXBean.setThreadCpuTimeEnabled(true);

    }


    public void performTimeTests(long totalTime, long step){
        this.CPUTime = false;
        this.firstTurn = true;

        buffer1.resetHandledRequest();
        performOneGroupTest(this.buffer1, totalTime, step);

        this.firstTurn = false;
        buffer2.resetHandledRequest();
        performOneGroupTest(this.buffer2, totalTime, step);
    }

    public void performCPUTests(long totalTime, long step){
        this.CPUTime = true;
        this.firstTurn = true;

        buffer1.resetHandledRequest();
        performOneGroupTest(this.buffer1, totalTime, step);

        this.firstTurn = false;
        buffer2.resetHandledRequest();
        performOneGroupTest(this.buffer2, totalTime, step);
    }


    private void performOneGroupTest(IBuffer buffer,long totalTime, long step){
        int totalSteps = (int) (totalTime / step);

        long startTime = getPerformanceTime(Thread.currentThread().getId());
        long stepTime = startTime;
        ArrayList<Consumer> consumers = initConsumers(buffer);
        ArrayList<Producer> producers = initProducers(buffer);

        long currentTime;
        long timeSpend; // Time which all process (Consumer and Producers) spends
        while(totalSteps > 0){

            if (getNanoTime() - stepTime > step){
                timeSpend = performStep(consumers, producers);

                if (this.CPUTime) buffer.updateHandledRequest(timeSpend);

                else{
                    currentTime = this.getNanoTime();
                    buffer.updateHandledRequest(currentTime - startTime);
                }
                totalSteps--;
                stepTime = getNanoTime();
            }
        }
        stopThreads(consumers, producers);
        savePerformance(consumers, producers);
    }

    private ArrayList<Producer> initProducers(IBuffer buffer){
        ArrayList<Producer> producers = new ArrayList<Producer>(this.noProducer);
        for(int i = 0; i < noProducer; i++){
            Producer producer = new Producer(i ,buffer);
            producer.start();
            producers.add(producer);
        }
        return producers;
    }

    private ArrayList<Consumer> initConsumers(IBuffer buffer){
        ArrayList<Consumer> consumers = new ArrayList<Consumer>(this.noConsumer);
        for(int i = 0; i < noConsumer; i++){
            Consumer consumer = new Consumer(i ,buffer);
            consumer.start();
            consumers.add(consumer);
        }
        return consumers;
    }

    private void stopThreads(ArrayList<Consumer> consumers, ArrayList<Producer> producers){
        for(Consumer consumer : consumers){
            consumer.stopRunning();
        }

        for(Producer producer: producers){
            producer.stopRunning();
        }
    }
    private long performStep(ArrayList<Consumer> consumers, ArrayList<Producer> producers){
        long timeSpend = 0L;
        long time;
        for(Consumer consumer : consumers){
            time = getPerformanceTime(consumer.getThreadID());
            consumer.updateTime(time);
            timeSpend += time;
        }

        for(Producer producer: producers){
            time = getPerformanceTime(producer.getThreadID());
            producer.updateTime(time);
            timeSpend += time;
        }

        return timeSpend;
    }

    private long getNanoTime(){
        return System.nanoTime();
    }

    private long getCPUTime(long threadID){
        return threadMXBean.getThreadCpuTime(threadID);
    }

    private long getPerformanceTime(long threadID){
        if (this.CPUTime) return getCPUTime(threadID);

        return getNanoTime();

    }

    private void savePerformance(ArrayList<Consumer> consumers, ArrayList<Producer> producers){
        if (this.firstTurn){
            consumers1 = consumers;
            producers1 = producers;
        }
        else{
            consumers2 = consumers;
            producers2 = producers;
        }
    }

    public ArrayList<ArrayList<TimeStamp>> getResults(){
        ArrayList<ArrayList<TimeStamp>> result = new ArrayList<>();

        ArrayList<TimeStamp> buffer1Result = new ArrayList<>(buffer1.getHandledRequestArray());
        ArrayList<TimeStamp> buffer2Result = new ArrayList<>(buffer2.getHandledRequestArray());

        result.add(buffer1Result);
        result.add(buffer2Result);
        return result;
    }

}
