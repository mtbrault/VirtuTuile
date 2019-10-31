package fr.virtutuile.domain;

public class Point {
    public Integer x;
    public Integer y;

    public Point(Integer x, Integer y) {
        this.x = new Integer(x);
        this.y = new Integer(y);
    }

    public Point(Point point) {
        this.x = new Integer(point.x);
        this.y = new Integer(point.y);
    }

    public Point() {
        this.x = new Integer(0);
        this.y = new Integer(0);
    }

    public void setPos(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
}
