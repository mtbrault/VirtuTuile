package fr.virtutuile.domain;

import java.util.ArrayList;
import java.util.List;

public class Surface extends Polygon {

    private List<Tile> tiles;
    private boolean selected;
    private Pattern pattern;
    private Material material;

    public Surface(List<Point> points) {
        super(points, PolygonType.SURFACE);
        tiles = new ArrayList<>();
    }

    public int getNbTiles() {
        return tiles.size();
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
        this.tiles = pattern.build(material, this);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public ArrayList<Point> getIntersectionsPointWithSurface(Surface surface) {
        ArrayList<Point> intersectionPoints = new ArrayList<Point>();
        for (Point point : surface.getPoints()) {
            if (isInside(point)) {
                System.out.println("intersection");
                intersectionPoints.add(new Point(point));
            }
        }
        for (Point point : points) {
            if (surface.isInside(point)) {
                System.out.println("intersection");
                intersectionPoints.add(new Point(point));
            }
        }
        return intersectionPoints;
    }
    public void onMoved() {
        tiles = pattern.build(material, this);
    }

    public boolean isSurfaceStacked(Surface surface) {
        for (Point point : points) {
            if (surface.isInside(point)) {
                return true;
            }
        }
        for (Point point : surface.getPoints()) {
            if (isInside(point)) {
                return true;
            }
        }
        return false;
    }
}
