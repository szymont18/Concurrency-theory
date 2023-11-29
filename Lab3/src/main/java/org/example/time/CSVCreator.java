package org.example.time;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class CSVCreator<T> {

    public CSVCreator(){

    }

    public void toCSV(HashMap<String, ArrayList<T>> files){
        for (String fileName : files.keySet()){
            ArrayList<T> content = files.get(fileName);

            try (CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName))){
                for(Object o: content){
                    String toWrite = o.toString();
                    csvWriter.writeNext(toWrite.split(","), false);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void toCSV(ArrayList<T> results, ArrayList<String> labels, String fileName){
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName))){
            StringBuilder sb = new StringBuilder();
            for (String label : labels){
                sb.append(label);
                sb.append(";");
            }
            sb.delete(sb.length() - 1, sb.length());
            String label = sb.toString();
            csvWriter.writeNext(label.split(","), false);
            for(Object o: results){
                String toWrite = o.toString();
                csvWriter.writeNext(toWrite.split(","), false);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
