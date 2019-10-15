package fr.virtutuile.domain;

import java.util.List;

public class Tile extends Polygon {
    private boolean feasable;


    public Tile(List<Point> points) {
        super(points, PolygonType.TILE);
    }

    private void checkFeasable() { }

    public boolean isFeasable() {
        return feasable;
    }
}
