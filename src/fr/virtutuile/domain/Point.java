package fr.virtutuile.domain;

public class Point {
    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
