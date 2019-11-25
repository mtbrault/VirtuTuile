package fr.virtutuile.domain;

import java.util.ArrayList;
import java.util.List;

public class Surface extends Polygon {

    private List<Tile> tiles;
    private boolean selected;
    private Pattern pattern;
    private Material material;
    private int jointSize = 0;
    private SurfaceType surfaceType = SurfaceType.REGULAR;
    private int patternId = 1;

    public Surface(List<Point> points) {
        super(points, PolygonType.SURFACE);
        tiles = new ArrayList<>();
    }

    public int getJointSize() {
        return jointSize;
    }

    public void changePattern() {
        this.patternId = (this.patternId + 1) % 2;
    }
    public int getPatternId() {
        return patternId;
    }

    public void setJointSize(int newJointSize) {
         jointSize = newJointSize;
    }

    public void setIrregular() {
        surfaceType = SurfaceType.IRREGULAR;
    }

    public SurfaceType getSurfaceType() {
        return surfaceType;
    }

    public int getHeight() {
        return Math.abs(points.get(0).y - points.get(3).y);
    }

    public void setHeight(int height) {
        this.points.get(3).y = this.points.get(0).y + height;
        this.points.get(2).y = this.points.get(0).y + height;
    }

    public void setWidth(int width) {
        this.points.get(1).x = this.points.get(0).x + width;
        this.points.get(2).x = this.points.get(0).x + width;
    }
    public int getWidth() {
        return Math.abs(points.get(0).x - points.get(1).x);
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
    public Material getMaterial() {
        return this.material;
    }
    public ArrayList<Point> getIntersectionsPointWithSurface(Surface surface) {
        ArrayList<Point> intersectionPoints = new ArrayList<Point>();
        for (Point point : surface.getPoints()) {
            if (isInside(point)) {
                intersectionPoints.add(new Point(point));
            }
        }
        for (Point point : points) {
            if (surface.isInside(point)) {
                intersectionPoints.add(new Point(point));
            }
        }
        return intersectionPoints;
    }
    public void onMoved() {
        tiles = pattern.build(material, this);
    }

    public SurfacePosition getPositionSurface(Surface surface) {
        List<Point> surfacePoints = surface.getPoints();
        int dist1 = Math.abs(points.get(0).x - surfacePoints.get(1).x);
        int dist2 = Math.abs(points.get(1).x - surfacePoints.get(0).x);
        int dist3 = Math.abs(points.get(0).y - surfacePoints.get(2).y);
        int dist4 = Math.abs(points.get(2).y - surfacePoints.get(0).y);
        if (dist1 == 0) {
            return SurfacePosition.LEFT;
        }
        if (dist2 == 0) {
            return SurfacePosition.RIGHT;
        }
        if (dist3 == 0) {
            return SurfacePosition.TOP;
        }
        if (dist4 == 0) {
            return SurfacePosition.BOTTOM;
        }
        return SurfacePosition.NONE;
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
