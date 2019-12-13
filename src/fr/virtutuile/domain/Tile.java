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

    public void setDetected(boolean value) {detected = value;}

}
