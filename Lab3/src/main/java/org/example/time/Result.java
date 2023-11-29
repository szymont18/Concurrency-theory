package org.example.time;


public class Result {

    private int elements;
    private double time = -1;
    private long timeL = -1;
    private String type;

    public Result(int elements, long timeL, String type) {
        this.elements = elements;
        this.timeL = timeL;
        this.type = type;
    }

    public Result(int elements, double time, String type) {
        this.elements = elements;
        this.time = time;
        this.type = type;
    }

    @Override
    public String toString() {
        if (time > 0)return elements+";"+time+";"+type;

        return elements+";"+timeL+";"+type;


    }
}
