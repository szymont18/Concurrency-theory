package org.example.time;

import org.example.IBuffer;
import org.example.Person;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimeProducer extends Thread implements Person {
    private static ThreadMXBean cpuMeter = ManagementFactory.getThreadMXBean();
    private IBuffer buffer;
    private int id;
    private int noProduce;

    private long noIteration;

    private long time;
    private long cpuTime;

    private Random generator;
    private boolean working;
    private ArrayList<Long> results;
    private Test testType;
    private ArrayList<Integer> noVal;
    public TimeProducer(IBuffer buffer, int id, int noConsume, long noIteration, Test testType){
        this.buffer = buffer;
        this.id = id;
        this.noProduce = noConsume;
        this.noIteration = noIteration;
        this.testType = testType;

        switch (testType){
            case TEST2 -> {
                generator = new Random(id);
                working = true;
                results = new ArrayList<>();
                noVal = new ArrayList<>();
                for(int i = 0; i < noConsume + 1; i++) {
                    results.add(0L);
                    noVal.add(0);
                }
            }
        }

    }

    public ArrayList<Double> getResults() {
        ArrayList<Double> meanResults = new ArrayList<>();
        for(int i = 0; i < results.size(); i++){
            if(noVal.get(i) != 0) meanResults.add((double) (results.get(i) / noVal.get(i)));
            else meanResults.add(0.0);
        }
        return meanResults;
    }

    @Override
    public void run() {
        switch (testType){
            case TEST1 -> {
                long startTime = System.nanoTime();
                for(int i = 0; i < noIteration; i++){

                    buffer.produce(this, noProduce);
                }
                this.time = System.nanoTime() - startTime;
                this.cpuTime = cpuMeter.getThreadCpuTime(getId());
            }

            case TEST2 -> {
                int val;
                long elapsed;
                while(working){
                    val = getRandomInt();
                    elapsed = System.nanoTime();
                    buffer.produce(this, val);

                    results.set(val, results.get(val) + System.nanoTime() - elapsed);
                    noVal.set(val, noVal.get(val) + 1);

                }
            }
        }



    }
    public void stopWorking(){
        working=false;
    }



    public long getTime() {
        return time;
    }

    public long getCpuTime() {
        return cpuTime;
    }

    @Override
    public String introduceYourself() {
        return String.valueOf(this.id);
    }

    @Override
    public int getRandomInt() {
        return this.generator.nextInt(noProduce) + 1;
    }
}