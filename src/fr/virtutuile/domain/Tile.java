package fr.virtutuile.domain;

import java.util.List;

public class Tile extends Polygon implements java.io.Serializable {
    private boolean selected;
    private boolean detected = false;

    public Tile(List<Point> points) {
        super(points, PolygonType.TILE);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isDetected() {return detected;}

    public Point getExtremePoint(int x, int y) {
        int value = (x == -1 || y == -1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        Point pointValue = null;

        for (Point point : points) {
            if (x == 1 && point.x > value) {
                value = point.x;
                pointValue = point;
            }
            else if (y == 1 && point.y >= value) {
                value = point.y;
                pointValue = point;
            }
            else if (x == -1 && point.x < value) {
                value = point.x;
                pointValue = point;
            }
            else if (y == -1 && point.y < value) {
                value = point.y;
                pointValue = point;
            }
        }
        return pointValue;
    }
    
    public void setDetected(boolean value) {detected = value;}

}
