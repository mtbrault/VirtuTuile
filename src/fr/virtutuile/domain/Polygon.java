package fr.virtutuile.domain;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Point> points;
    private PolygonType type;
    private float area;
    private Color color;
    private List<String> labels;
    private boolean isOnLine(Point p1, Point p2, Point p3) {
        if (p3.x < Math.max(p1.x, p2.x) && p3.y < Math.max(p1.y, p2.y) && p3.y > Math.min(p1.y, p2.y)) {
            return true;
        } else if (p1.x >= p3.x && p1.y == p3.y) {
            return true;
        }
        return false;
    }

    public Polygon(List<Point> points, PolygonType type) {
        this.points = points;
        this.type = type;
        this.color = new Color();
    }

    public List<Point> getPoints() {
        return points;
    }

    public float getArea() {
        return area;
    }

    public PolygonType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isInside(Point p3) {
        int nbSegment = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            if (isOnLine(points.get(i), points.get(i + 1), p3)) {
                nbSegment++;
            }
        }
        if (isOnLine(points.get(0), points.get(points.size() - 1), p3)) {
            nbSegment++;
        }
        if (nbSegment % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
    public void move(Point newPoint) {

    }
}
