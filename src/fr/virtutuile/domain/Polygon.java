package fr.virtutuile.domain;

import java.util.List;

public class Polygon {
    private List<Point> points;
    private PolygonType type;
    private float area;
    private Color color;

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

    public boolean isInside(Point point) {
        return false;
    }

    public void move(Point newPoint) {

    }
}
