package org.example.time;

import org.example.Consumer;
import org.example.IBuffer;
import org.example.Producer;
import org.example.conditions2.RandomBuffer;
import org.example.conditions4.StarvationFreeBuffer;
import org.example.lock3.ThreeLockBuffer;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

public class MainTime {
    public static void main(String[] args) {
        /*
        Test 1
        Params:
            * testTime - time of the test
            * maxBuffer - capacity of the buffer
            * noPerson - number of people of certain type (noPerson = 10 <=> noConsumers + noProducers = 20)
            * maxRequest - boundary for request

        Description:
            Every time, everyone draws the number of elements to produce/consume. Before they came into buffer, the clock
            starts. When they have finally produced/consumed the clock shuts down. The results are written to result Array
            on the index of produced/consumed elements. Finally, the mean time in buffer is returning for every number of request.
         */

        long testTime = 300000L;
        int maxBuffer = 100;
        int noPerson = 10;
        int maxRequest = 50;
        String extension = ".csv";
        ArrayList<IBuffer> buffersToTest = new ArrayList<>(Arrays.asList(
                                                                        new ThreeLockBuffer(maxBuffer),
                                                                        new StarvationFreeBuffer(maxBuffer)
        ));

        ArrayList<String> filenames = new ArrayList<>(Arrays.asList("threeLock", "fourCondition"));
        ArrayList<String> labels = new ArrayList<>(Arrays.asList("elements", "time", "buffer"));
        CSVCreator<Result> csvCreator = new CSVCreator<>();
        int bufferID = 0;
        for(IBuffer buffer : buffersToTest) {
            System.out.println(buffer);
            ArrayList<TimeConsumer> timeConsumers = new ArrayList<>();

            ArrayList<TimeProducer> timeProducers = new ArrayList<>();

            for (int i = 0; i < noPerson; i++) {
                TimeConsumer consumer = new TimeConsumer(buffer, i,  maxRequest, Test.TEST1);
                timeConsumers.add(consumer);
                consumer.start();
                TimeProducer producer = new TimeProducer(buffer, i,  maxRequest, Test.TEST1);
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
                    nanoTimesDouble.set(j, nanoTimesDouble.get(j) + partResults1.get(j));
                    nanoTimesDouble.set(j, nanoTimesDouble.get(j) + partResults2.get(j));
                }
            }

            // Get Mean
            for(int i = 0; i < maxRequest + 1; i++){
                nanoTimesDouble.set(i, nanoTimesDouble.get(i) / noPerson);
            }

            ArrayList<Result> nanoTimes = new ArrayList<>();
            for(int i = 0; i < maxRequest + 1; i++){
                nanoTimes.add(new Result(i, nanoTimesDouble.get(i), filenames.get(bufferID)));
            }

            csvCreator.toCSV(nanoTimes, labels, filenames.get(bufferID)+extension);
            bufferID++;
        }

        /*
        Test 2 - test for noRound = 1
        Test 3 - test for noRound > 1
        Params: * noPerson
                * noRound
                * timeBetweenRounds
                * maxRequest
                * maxBuffer

        Description:
            Set the time test. After each step measure how much each Thread produce/consume. Every Thread has got set
            certain amount of elements to produce or consume.
        Description:
            Count numbers of all produced/consumed element during period of time
         */

//        int noPerson = 10;
//        int noRound = 1;
//        long timeBetweenRounds = 300000; // milliseconds
//        int maxBuffer = 100;
//        int maxRequest = 10;
//
//        int requestStep = maxRequest / noPerson;
//        String extension = ".csv";
//        ArrayList<IBuffer> buffersToTest = new ArrayList<>(Arrays.asList(
//                new StarvationFreeBuffer(maxBuffer),
//                new ThreeLockBuffer(maxBuffer)
//
//        ));
//
//        ArrayList<String> filenames = new ArrayList<>(Arrays.asList(
//                                                                    "fourCondition",
//                                                                    "threeLock"
//                                                                    ));
//        ArrayList<String> labels = new ArrayList<>(Arrays.asList("elements_per_request", "elements_taken", "buffer"));
//        ArrayList<String> labelsSpeed = new ArrayList<>(Arrays.asList("time", "element_taken", "buffer"));
//
//        CSVCreator<Result> csvCreator = new CSVCreator<>();
//
//        int bufferID = 0;
//        for (IBuffer buffer : buffersToTest) {
//            System.out.println(buffer);
//            Consumer[] consumers = new Consumer[noPerson];
//            Producer[] producers = new Producer[noPerson];
//            for(int i = 0; i < noPerson; i++){
//                Consumer consumer = new Consumer(i+1, buffer, (i + 1) * requestStep);
//                consumers[i] = consumer;
//
//                consumer.start();
//                Producer producer = new Producer(i + 1, buffer, (i + 1) * requestStep);
//                producer.start();
//                producers[i] = producer;
//            }
//
//
//            ArrayList<Result> resultsToSave = new ArrayList<>();
//            ArrayList<Result> bufferResult = new ArrayList<>();
//            long periodResult;
//            for(int round = 0; round < noRound; round++){
//
//                try {
//                    Thread.sleep(timeBetweenRounds);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                periodResult = 0;
//                for(int i = 0; i < noPerson; i++){
//                    // Number of elements taken by Consumer[i] and Producer[i]
//                    resultsToSave.add(new Result((long) (i + 1) * requestStep, consumers[i].getNoConsumed() + producers[i].getNoProduced(),
//                                                                                    filenames.get(bufferID)));
//
//                    periodResult = periodResult + consumers[i].getNoConsumed() + producers[i].getNoProduced();
//                }
//
//                bufferResult.add(new Result((round + 1) * timeBetweenRounds, periodResult, filenames.get(bufferID)));
//
//            }
//
//            // Kill Producers and Consumers
//            for (int i = 0; i < noPerson; i++) {
//                consumers[i].stopRunning();
//                producers[i].stopRunning();
//            }
//            // Save to file
//            csvCreator.toCSV(resultsToSave, labels, filenames.get(bufferID)+extension);
//            csvCreator.toCSV(bufferResult, labelsSpeed, filenames.get(bufferID)+ "speed" +extension);
//            bufferID++;
//        }
//        System.out.println("End working...");
//        System.exit(0);

    }
}
