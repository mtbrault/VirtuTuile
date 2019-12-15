package fr.virtutuile.domain;

import java.util.List;

public class Polygon implements java.io.Serializable {
    protected List<Point> points;
    private PolygonType type;
    private String color = "#FFFFFF";


    private boolean isOnLine(Point p1, Point p2, Point p3) {
        if (p3.x < Math.max(p1.x, p2.x) && p3.y < Math.max(p1.y, p2.y) && p3.y > Math.min(p1.y, p2.y)) {
            return true;
        } else if (p1.x >= p3.x && p1.y == p3.y) {
            return true;
        }
        return false;
    }

    public boolean isInside(Point p3) {
        int nbSegment = 0;
        System.out.println("");
        for (int i = 0; i < points.size() - 1; i++) {
            if (isOnLine(points.get(i), points.get(i + 1), p3)) {
                nbSegment++;
            }
        }
        if (isOnLine(points.get(0), points.get(points.size() - 1), p3)) {
            nbSegment++;
        }
        return (nbSegment % 2 == 1);
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
