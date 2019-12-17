package fr.virtutuile.domain;

import java.util.List;

public class Hole extends Polygon {

    public Hole(List<Point> points) {
        super(points, PolygonType.HOLE);
    }

}