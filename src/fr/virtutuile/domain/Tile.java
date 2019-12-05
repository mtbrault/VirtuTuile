package fr.virtutuile.domain;

import java.util.List;

public class Tile extends Polygon implements java.io.Serializable {
    private boolean selected;

    public Tile(List<Point> points) {
        super(points, PolygonType.TILE);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

}
