package fr.virtutuile.domain;

import java.util.List;

public class Tile extends Polygon {
    private boolean feasable;
    private boolean selected;

    public Tile(List<Point> points) {
        super(points, PolygonType.TILE);
    }

    private void checkFeasable() { }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }


    public boolean isFeasable() {
        return feasable;
    }
}
