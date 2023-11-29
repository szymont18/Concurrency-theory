package org.example.time;

import org.example.IBuffer;
import org.example.conditions2.RandomBuffer;
import org.example.conditions4.StarvationFreeBuffer;
import org.example.lock3.ThreeLockBuffer;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

public class MainTime {
    public static void main(String[] args){
        /*
        Test 1
        Every Producer and Consumer produce/consume certain number of elements. Everybody has got two timers: CPU and
        nano-system. After certain action, the timers are stopping.
        This test should measure whether consumers and producers are starving and which buffer performs better.


        long noIteration = 1000000000L;
        int maxBuffer = 100;
        int noPerson = 10;
        int consumeStep = (maxBuffer / 2) / noPerson;
        String extension = ".csv";
        ArrayList<IBuffer> buffersToTest = new ArrayList<>(Arrays.asList(new RandomBuffer(maxBuffer),new ThreeLockBuffer(maxBuffer),
                                                                        new StarvationFreeBuffer(maxBuffer)
                                                                        ));

        ArrayList<String> filenames = new ArrayList<>(Arrays.asList("twoConditions", "threeLock", "fourCondition"));
        ArrayList<String> labels = new ArrayList<>(Arrays.asList("elements", "time", "buffer"));
        CSVCreator<Result> csvCreator = new CSVCreator<>();
        int bufferID = 0;
        for(IBuffer buffer : buffersToTest) {
            System.out.println(buffer);
            ArrayList<TimeConsumer> timeConsumers = new ArrayList<>();


            ArrayList<TimeProducer> timeProducers = new ArrayList<>();

            for (int i = 0; i < noPerson; i++) {
                TimeConsumer consumer = new TimeConsumer(buffer, i, (i + 1) * consumeStep, noIteration);
                timeConsumers.add(consumer);
                consumer.start();
                TimeProducer producer = new TimeProducer(buffer, i, (i + 1) * consumeStep, noIteration);
                timeProducers.add(producer);
                producer.start();
            }
            for(int i = 0; i < noPerson; i++){
                try {
                    timeConsumers.get(i).join();
                    timeProducers.get(i).join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            ArrayList<Result> nanoTimes = new ArrayList<Result>();
            ArrayList<Result> cpuTimes = new ArrayList<Result>();
            for (int i = 0; i < noPerson; i++) {
                nanoTimes.add(new Result((i + 1) * consumeStep, timeConsumers.get(i).getTime() +
                        timeProducers.get(i).getTime(),  filenames.get(bufferID)));

                cpuTimes.add(new Result((i + 1) * consumeStep, timeConsumers.get(i).getCpuTime() +
                        timeProducers.get(i).getCpuTime(),  filenames.get(bufferID)));
            }
            csvCreator.toCSV(nanoTimes, labels, filenames.get(bufferID)+"nano"+extension);
            csvCreator.toCSV(cpuTimes, labels, filenames.get(bufferID)+"cpu"+extension);
            bufferID++;
        }
        */

        /*
        Test 2
        Tak jak na zajęciach:
         */
        long testTime = 100000L;
        long noIteration = 1000000000L;
        int maxBuffer = 100;
        int noPerson = 10;
        int maxRequest = 50;
        String extension = ".csv";
        ArrayList<IBuffer> buffersToTest = new ArrayList<>(Arrays.asList(new RandomBuffer(maxBuffer)
//                                                                        new ThreeLockBuffer(maxBuffer),
//                                                                        new StarvationFreeBuffer(maxBuffer)
        ));

        ArrayList<String> filenames = new ArrayList<>(Arrays.asList("twoConditions", "threeLock", "fourCondition"));
        ArrayList<String> labels = new ArrayList<>(Arrays.asList("elements", "time", "buffer"));
        CSVCreator<Result> csvCreator = new CSVCreator<>();
        int bufferID = 0;
        for(IBuffer buffer : buffersToTest) {
            System.out.println(buffer);
            ArrayList<TimeConsumer> timeConsumers = new ArrayList<>();


            ArrayList<TimeProducer> timeProducers = new ArrayList<>();

            for (int i = 0; i < noPerson; i++) {
                TimeConsumer consumer = new TimeConsumer(buffer, i,  maxRequest, noIteration, Test.TEST2);
                timeConsumers.add(consumer);
                consumer.start();
                TimeProducer producer = new TimeProducer(buffer, i,  maxRequest, noIteration, Test.TEST2);
                timeProducers.add(producer);
                producer.start();
            }
            try {
                Thread.sleep(testTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            for(int i = 0; i < noPerson; i++){
                timeConsumers.get(i).stopWorking();
                timeProducers.get(i).stopWorking();
            }
            System.out.println("Done");

            ArrayList<Double> nanoTimesDouble = new ArrayList<Double>();
            for(int i = 0; i < maxRequest + 1; i++){
                nanoTimesDouble.add(0.0);
            }

            for(int i = 0; i < noPerson; i++){
                ArrayList<Double> partResults1 = timeConsumers.get(i).getResults();
                ArrayList<Double> partResults2 = timeProducers.get(i).getResults();
                for(int j = 0; j < maxRequest + 1; j++){
                    nanoTimesDouble.add(j, partResults1.get(j));
                    nanoTimesDouble.add(j, partResults2.get(j));
                }
            }


            ArrayList<Result> nanoTimes = new ArrayList<>();
            for(int i = 0; i < maxRequest + 1; i++){
                nanoTimes.add(new Result(i, nanoTimesDouble.get(i), filenames.get(bufferID)));
            }

            csvCreator.toCSV(nanoTimes, labels, filenames.get(bufferID)+"nano"+extension);
            bufferID++;
        }

        /*
        Test 3
        Set the time test. After each step measure how much each Thread produce/consume. Every Thread has got set
        certain amount of elements to produce or consume.
         */

    }
}
