package fr.virtutuile.domain;

import java.awt.event.HierarchyBoundsAdapter;
import java.util.ArrayList;
import java.util.List;

public class Surface extends Polygon {

    private List<Tile> tiles;
    private List<Hole> holes = new ArrayList<>();
    private boolean selected;
    private Pattern pattern = new Pattern();
    private Material material;
    private int jointSize = 0;
    private SurfaceType surfaceType = SurfaceType.REGULAR;
    private int patternId = 0;
    private double tileShift = 0.5;
    private boolean vertical = true;

    public Surface(List<Point> points) {
        super(points, PolygonType.SURFACE);
        tiles = new ArrayList<>();
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical() {
        vertical = !vertical;
    }

    public Surface(Surface surface) {
        super(surface.getPoints(), PolygonType.SURFACE);
        this.points = copyPoint(this.points);
        this.tiles = new ArrayList<Tile>(surface.getTiles());
        this.holes = copyHoles(surface.getHoles()); //La fonction copyHoles doit être mis à jour
        this.selected = surface.isSelected();
        this.pattern = surface.getPattern();
        this.material = surface.getMaterial();
        this.jointSize = surface.getJointSize();
        this.surfaceType = surface.getSurfaceType();
        this.patternId = surface.getPatternId();
    }

    private List<Point> copyPoint(List<Point> points) {
        List<Point> list = new ArrayList<Point>();
        for (Point point : points)
            list.add(new Point(point));
        return list;
    }

    private List<Hole> copyHoles(List<Hole> holes) {
        return holes;
        /*List<Polygon> list = new ArrayList<Polygon>();
        for (Polygon poly : holes)
            list.add(new Polygon(poly, poly.get));
        return list;*/
    }

    public Pattern getPattern() {
        return pattern;
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

    public double getTileShift() {
        return this.tileShift;
    }

    public void setTileShift(double tileShift) {
        this.tileShift = tileShift;
    }

    public void addPoint(Point lastPoint) {
        points.add(lastPoint);
    }
    public SurfaceType getSurfaceType() {
        return surfaceType;
    }

    public int getHeight() {
        if (SurfaceType.REGULAR == surfaceType) {
            return Math.abs(points.get(0).y - points.get(3).y);
        } else {
            return Math.abs(points.get(0).y - points.get(1).y);
        }
    }

    public void digHole(List<Point> list) {
        for (Point points : list) {
            if (!isInside(points))
                return;
        }
        Hole h = new Hole(new ArrayList<Point>(list));
        holes.add(h);
    }

    public List<Hole> getHoles() {
        return (holes);
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
        if (material != null) {
            if (pattern != null) {
                tiles = pattern.build(material, this);
            }
        }
    }

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


    public Surface getExtremeSurface() {
        Point left = getExtremePoint(-1,0);
        Point right = getExtremePoint( 1,0);
        Point down = getExtremePoint( 0,1);
        Point up = getExtremePoint(0,-1);
        Point point1 = new Point(left.x, up.y);
        Point point2 = new Point(right.x, up.y);
        Point point3 = new Point(right.x, down.y);
        Point point4 = new Point(left.x, down.y);
        return new Surface(new ArrayList<Point>(){
            {add(point1);
                add(point2);
                add(point3);
                add(point4);}
        });
    }

    public void translatePoint(int x, int y) {
        for (Point point : points)
            point.add(new Point(x, y));
    }
}
