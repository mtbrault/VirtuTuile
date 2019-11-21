package fr.virtutuile.domain;

public class Point {
    public Integer x;
    public Integer y;
    public Integer initX;
    public Integer initY;
    public Integer zoomX;
    public Integer zoomY;

    public Point(Integer x, Integer y) {
        this.x = new Integer(x);
        this.y = new Integer(y);
        this.zoomX = new Integer(x);
        this.zoomY = new Integer(y);
    }

    public Point(Point point) {
        this.x = new Integer(point.x);
        this.y = new Integer(point.y);
        this.zoomX = new Integer(point.zoomX);
        this.zoomY = new Integer(point.zoomY);
    }

    public Point() {
        this.x = new Integer(0);
        this.y = new Integer(0);
        this.zoomX = new Integer(0);
        this.zoomY = new Integer(0);
    }

    public Point add(Point point) {
        x += point.x;
        y += point.y;
        return this;
    }
    public Point less(Point point) {
        x -= point.x;
        y -= point.y;
        return this;
    }
    public void setPos(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void setZoom(Integer x, Integer y) {
        this.zoomX = x;
        this.zoomY = y;
    }
}
