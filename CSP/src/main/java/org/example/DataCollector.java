package org.example;

import org.jcsp.lang.CSProcess;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataCollector extends Thread {
    static String CSV_FILE_NAME = "result.csv";
    private Buffer[] buffers;
    private long time;
    private Consumer[] consumers;

    public DataCollector(Buffer[] buffers, Consumer[] consumers, long time) {
        this.buffers = buffers;
        this.time = time;
        this.consumers = consumers;
    }


    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        System.out.println("Start");
        String[] output = new String[consumers.length];
        System.out.println(System.currentTimeMillis() - startTime);

        while(System.currentTimeMillis() - startTime < time){

            for(int i = 0; i < consumers.length; i++){
                output[i] = String.valueOf(consumers[i].getJumps());
            }
        }
        System.out.println("Koniec testu");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(CSV_FILE_NAME);
            for(int i = 0; i < consumers.length; i++){
                fileWriter.write(output[i] + "\n");
            }
            if (fileWriter != null) {
                fileWriter.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.exit(0);
    }

    /*
    FOR BANDWITH
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        System.out.println("Start");
        String[] output = new String[buffers.length];
        System.out.println(System.currentTimeMillis() - startTime);
        long sum;
        long interval = 10000;
        long lastMeassure = startTime;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(CSV_FILE_NAME);
            while (System.currentTimeMillis() - startTime < time) {

                if (System.currentTimeMillis() - lastMeassure > interval) {
                    sum = 0L;
                    lastMeassure = System.currentTimeMillis();
                    for (int i = 0; i < buffers.length; i++) {
                        sum += buffers[i].getCounter();
                    }
                    fileWriter.write(String.valueOf(sum));
                    fileWriter.write("\n");
                }
            }
            fileWriter.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        System.out.println("Koniec testu");
//        FileWriter fileWriter = null;
//        try {
//            fileWriter = new FileWriter(CSV_FILE_NAME);
//            for(int i = 0; i < buffers.length; i++){
//                fileWriter.write(output[i] + "\n");
//            }
//            if (fileWriter != null) {
//                fileWriter.close();
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


        System.exit(0);
    }
    */


}
