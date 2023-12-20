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

    public DataCollector(Buffer[] buffers, long time) {
        this.buffers = buffers;
        this.time = time;
    }


    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        System.out.println("Start");
        String[] output = new String[buffers.length];
        System.out.println(System.currentTimeMillis() - startTime);

        while(System.currentTimeMillis() - startTime < time){

            for(int i = 0; i < buffers.length; i++){
                output[i] = String.valueOf(buffers[i].getCounter());
            }
        }
        System.out.println("Koniec testu");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(CSV_FILE_NAME);
            for(int i = 0; i < buffers.length; i++){
                fileWriter.write(output[i] + ";");
            }
            if (fileWriter != null) {
                fileWriter.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.exit(0);
    }


}
