package org.example.time;

public class TimeStamp {


    private float time;
    private int elements;

    public TimeStamp(float time, int elements) {
        this.time = time;
        this.elements = elements;
    }

    @Override
    public String toString() {
        return time + "," + elements;
    }

    public float getVelocity(){
        return elements/time;
    }
}
