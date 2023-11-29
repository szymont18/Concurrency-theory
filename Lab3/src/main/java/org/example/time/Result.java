package org.example.time;


public class Result {

    private long x;
    private double y = -1;
    private long yL = -1;
    private String type;

    public Result(long x, long yL, String type) {
        this.x = x;
        this.yL = yL;
        this.type = type;
    }

    public Result(long x, double y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }




    @Override
    public String toString() {
        if (y > 0)return x +";"+ y +";"+type;

        return x +";"+ yL +";"+type;


    }
}
