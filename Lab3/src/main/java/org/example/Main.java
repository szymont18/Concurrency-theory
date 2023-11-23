package org.example;

import org.example.conditions2.RandomBuffer;
import org.example.conditions4.StarvationFreeBuffer;
import org.example.lock3.ThreeLockBuffer;
import org.example.time.CSVCreator;
import org.example.time.TimeMeter;
import org.example.time.TimeStamp;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
/*
        int noConsumer = 4;
        int noProducer = 4;
        int noRound = 10;
        int timeBetweenRounds = 5000; // milliseconds

        // Consumer 1 is one who has to starve.

        // Choose buffer type:
//        RandomBuffer buffer = new RandomBuffer(50);
//        StarvationFreeBuffer buffer = new StarvationFreeBuffer(50);
//        BufferHasWaiters buffer = new BufferHasWaiters(50);
        ThreeLockBuffer buffer = new ThreeLockBuffer(50);

        Consumer[] consumers = new Consumer[noConsumer];

        for(int i = 0; i < noConsumer; i++){
            Consumer consumer = new Consumer(i+1, buffer);
            consumers[i] = consumer;
            consumer.start();
        }

        for (int i = 0; i < noProducer; i++){
            Producer producer = new Producer(i + 1, buffer);
            producer.start();
        }

        for(int round = 0; round < noRound; round++){
            System.out.println("Round " + (round+1));
            for(int i = 0; i < noConsumer; i++){
                System.out.println("Consumer " + (i + 1) + " consumed " + consumers[i].getNoConsumed() + " times");
            }
            System.out.println();
            // Wait some time
            try {
                Thread.sleep(timeBetweenRounds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
          }
        */

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
//        System.out.println(threadMXBean.isThreadCpuTimeSupported());


        int noConsumer = 4;
        int noProducer = 4;
        long time = 10L * 1000000000;
        long stamp = 1L * 1000000000;

        ThreeLockBuffer threeLockBuffer = new ThreeLockBuffer(50);
        StarvationFreeBuffer starvationFreeBuffer = new StarvationFreeBuffer(50);
        TimeMeter timeMeter = new TimeMeter(threeLockBuffer, starvationFreeBuffer, noConsumer, noProducer);

        System.out.println("Time test 1...");

        timeMeter.performTimeTests(time, stamp);
        ArrayList<ArrayList<TimeStamp>> timeResult = timeMeter.getResults();
        System.out.println("Three Lock:" + timeResult.get(0).get(timeResult.get(0).size() - 1).getVelocity());
        System.out.println("Four Condition:" + timeResult.get(1).get(timeResult.get(1).size()- 1).getVelocity());


        System.out.println("Time test 2...");
        timeMeter.performCPUTests(time*10, stamp*10);
        ArrayList<ArrayList<TimeStamp>> CPUTimeResult = timeMeter.getResults();

        System.out.println("Three Lock:" + CPUTimeResult.get(0).get(CPUTimeResult.get(0).size() - 1).getVelocity());
        System.out.println("Four Condition:" + CPUTimeResult.get(1).get(CPUTimeResult.get(1).size() - 1).getVelocity());

        HashMap<String, ArrayList<TimeStamp>> csvConfig = new HashMap<String, ArrayList<TimeStamp>>(){{
            put("threeLockTime.csv", timeResult.get(0));
            put("fourConditionsTime.csv", timeResult.get(1));
            put("threeLockCPU.csv", CPUTimeResult.get(0));
            put("fourConditionsCPU.csv", CPUTimeResult.get(1));
        }};

        CSVCreator<TimeStamp> csvCreator = new CSVCreator<>();
        csvCreator.toCSV(csvConfig);


        System.exit(0);
    }


}