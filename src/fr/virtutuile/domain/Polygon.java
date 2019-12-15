package fr.virtutuile.domain;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.List;

public class Polygon implements java.io.Serializable {
    protected List<Point> points;
    private PolygonType type;
    private String color = "#FFFFFF";

    public boolean isInside(Point p3) {
        Area awtShape = Pattern.convertPolygonToShape(this);
        if (awtShape.contains(new java.awt.Point(p3.x, p3.y))) {
            return true;
        }
        return false;
    }

    public Polygon(List<Point> points, PolygonType type) {
        this.points = points;
        this.type = type;
    }

    public List<Point> getPoints() {
        return points;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void move(int x, int y) {
        for (Point point : points) {
            point.x += x;
            point.y += y;
        }
    }
}
