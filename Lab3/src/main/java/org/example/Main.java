package org.example;

import org.example.conditions2.RandomBuffer;
import org.example.conditions4.StarvationFreeBuffer;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;


public class Main {
    static long seed = 25L;

    public static void main(String[] args) throws InterruptedException {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
//        System.out.println(threadMXBean.isThreadCpuTimeSupported());

        int noConsumer = 10;
        int noProducer = 10;
        int noRound = 10;
        int timeBetweenRounds = 5000; // milliseconds

        // Consumer 1 is one who has to starve.

        // Choose buffer type:
        RandomBuffer buffer = new RandomBuffer(100);
//        StarvationFreeBuffer buffer = new StarvationFreeBuffer(100);
//        ThreeLockBuffer buffer = new ThreeLockBuffer(100);

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
            int noConsumed = 0;
            System.out.println("Round " + (round+1));
            for(int i = 0; i < noConsumer; i++){
                System.out.println("Consumer " + (i + 1) + " consumed " + consumers[i].getNoConsumed() + " times");
                noConsumed += consumers[i].getNoConsumed();
            }
            System.out.println("Overall " + noConsumed);
            System.out.println();

            try {
                Thread.sleep(timeBetweenRounds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

//
//        int noConsumer = 4;
//        int noProducer = 4;
//        long time = 100L * 1000000000;
//        long stamp = 1L * 1000000000;
//
//        ThreeLockBuffer threeLockBuffer = new ThreeLockBuffer(50);
//        StarvationFreeBuffer starvationFreeBuffer = new StarvationFreeBuffer(50);
//        RandomBuffer randomBuffer = new RandomBuffer(50);
//        TimeMeter timeMeter = new TimeMeter(noConsumer, noProducer);
//
//
//        System.out.println("Three Lock Tests...");
//
//        ArrayList<TimeStamp>threeLockResult = timeMeter.performTimeTests(threeLockBuffer,time, stamp);
//        ArrayList<TimeStamp>threeLockCPU = timeMeter.performCPUTests(threeLockBuffer,time, stamp);
//
//        System.out.println("Three Lock:" + threeLockResult.get(threeLockResult.size() - 1).getVelocity() );
//        System.out.println("Three Lock CPU:" + threeLockCPU.get(threeLockCPU.size() - 1).getVelocity());
//
//        System.out.println("Two Condition Test...");
//
//        ArrayList<TimeStamp> twoConditionResult = timeMeter.performTimeTests(randomBuffer,time, stamp);
//        ArrayList<TimeStamp> twoConditionCPU = timeMeter.performCPUTests(randomBuffer,time, stamp);
//
//        System.out.println("Two Condition:" + twoConditionResult.get(twoConditionResult.size() - 1).getVelocity() );
//        System.out.println("Two Condition CPU:" + twoConditionCPU.get(twoConditionCPU.size() - 1).getVelocity());
//
//        System.out.println("Four Condition Test...");
//
//        ArrayList<TimeStamp> fourConditionResult = timeMeter.performTimeTests(starvationFreeBuffer,time, stamp);
//        ArrayList<TimeStamp>fourConditionCPU = timeMeter.performCPUTests(starvationFreeBuffer,time, stamp);
//
//        System.out.println("Four Condition:" + fourConditionResult.get(fourConditionResult.size() - 1).getVelocity() );
//        System.out.println("Four Condition CPU:" + fourConditionCPU.get(fourConditionCPU.size() - 1).getVelocity());
//
//
//
//        HashMap<String, ArrayList<TimeStamp>> csvConfig = new HashMap<String, ArrayList<TimeStamp>>(){{
//            put("twoCondition.csv", twoConditionResult);
//            put("twoConditionCPU.csv", twoConditionCPU);
//            put("fourConditionsTime.csv", fourConditionResult);
//            put("fourConditionsCPU.csv", fourConditionCPU);
//            put("threeLockTime.csv", threeLockResult);
//            put("threeLockCPU.csv", threeLockCPU);
//        }};
//
//        CSVCreator<TimeStamp> csvCreator = new CSVCreator<>();
//        csvCreator.toCSV(csvConfig);
//
//
//        System.exit(0);
    }


}