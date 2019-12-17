package fr.virtutuile.domain;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

import java.awt.event.HierarchyBoundsAdapter;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
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
    private boolean isMasked = true;

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
        this.type = surface.getPolygonType();
        this.points = copyPoint(this.points);
        this.tiles = new ArrayList<Tile>(surface.getTiles());
        this.holes = copyHoles(surface.getHoles());
        this.selected = surface.isSelected();
        this.pattern = surface.getPattern();
        this.material = surface.getMaterial();
        this.jointSize = surface.getJointSize();
        this.surfaceType = surface.getSurfaceType();
        this.patternId = surface.getPatternId();
        this.tileShift = surface.getTileShift();
        this.vertical = surface.isVertical();
        this.isMasked = surface.getMasked();
        this.surfaceType = surface.getSurfaceType();
        setColor(surface.getColor());
        setPattern(surface.getPattern());
    }

    private List<Point> copyPoint(List<Point> points) {
        List<Point> list = new ArrayList<Point>();
        for (Point point : points)
            list.add(new Point(point));
        return list;
    }

    private List<Hole> copyHoles(List<Hole> holes) {
        List<Hole> list = new ArrayList<>();
        List<Point> points = new ArrayList<>();
        for (Hole poly : holes) {
            points.clear();
            for (Point tmp : poly.getPoints())
                points.add(new Point(tmp.x, tmp.y));
            list.add(new Hole(points));
        }
        return list;
    }

    public void setMasked(boolean masked) {
    	this.isMasked = masked;
    }
    
    public boolean getMasked() {
    	return this.isMasked;
    }
    
    @Override
    public void move(int x, int y) {
        super.move(x, y);
        for (Hole hole : getHoles()) {
            hole.move(x, y);
        }
        for (Tile tile : getTiles()) {
            tile.move(x, y);
        }
    }

    @Override
    public boolean isInside(Point p3) {
        boolean inHole = false;
        for (Hole hole : getHoles()) {
            if (hole.isInside(p3))
                inHole = true;
        }
        return super.isInside(p3) && !inHole;
    }

    public boolean isInsideIgnoreHole(Point p3) {
        return super.isInside(p3);
    }

    public Pattern getPattern() {
        return pattern;
    }

    public int getJointSize() {
        return jointSize;
    }

    public void changePattern(int id) {
        this.patternId = id;
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
        return Math.abs(getExtremePoint(0, 1).y - getExtremePoint(0, -1).y);
    }

    public int getNbBoxNeedForMaterial() {
        if (tiles == null || material == null)
            return -1;
        int box = tiles.size() / material.getNbTileByBox();
        if (tiles.size() % material.getNbTileByBox() != 0) {
            box++;
        }
        return box;
    }

    public int getNbTiles() {
        return this.tiles.size();
    }

    public Hole mergeHoles(Hole a, Hole b) {
        Area awtA = Pattern.convertPolygonToShape(a);
        Area awtB = Pattern.convertPolygonToShape(b);
        awtA.add(awtB);
        return (Hole)Pattern.convertShapeToPolygon(awtA, PolygonType.HOLE);
    }

    public boolean checkHoleCollision(Hole a, Hole b) {
        Area awtA = Pattern.convertPolygonToShape(a);
        Area awtB = Pattern.convertPolygonToShape(b);
        awtA.intersect(awtB);
        awtA.intersect(new Area(awtB));
        return (!awtA.isEmpty());
    }

    public void joinHoles(Hole toAdd) {
        List<Hole> newList = new ArrayList<>();
        for (Hole hole : getHoles()) {
            if (checkHoleCollision(toAdd, hole)) {
                toAdd = mergeHoles(toAdd, hole);
            } else {
                newList.add(hole);
            }
        }
        newList.add(toAdd);
        holes = newList;
    }

    public void digHole(List<Point> list) {
        int count = 0;
        for (Point points : list) {
            if (!isInsideIgnoreHole(points))
                count += 1;
        }
        if (count == list.size())
            return;
        joinHoles(new Hole(new ArrayList<Point>(list)));
    }

    public List<Hole> getHoles() {
        return (holes);
    }
    public void setHoles(List<Hole> h) {
        holes = h;
    };

    public void setHeight(int height) {
        double topExtremePointY = getExtremePoint(0, -1).y;
        double currentHeight = Math.abs(topExtremePointY - getExtremePoint(0, 1).y);
        double diff = height - currentHeight;
        for (Point point : points) {
            if (point.y != topExtremePointY){
                double add = (point.y - topExtremePointY) / currentHeight * diff;
                point.add(new Point(0, (int)Math.round(add)));
            }
        }
        for (Hole hole : holes) {
            for (Point point : hole.getPoints()) {
                if (point.y != topExtremePointY){
                    double add = (point.y - topExtremePointY) / currentHeight * diff;
                    point.add(new Point(0, (int)Math.round(add)));
                }
            }
        }
    }

    public void setWidth(int width) {
        double leftExtremePointX = getExtremePoint(-1, 0).x;
        double currentWidth = Math.abs(leftExtremePointX - getExtremePoint(1, 0).x);
        double diff = width - currentWidth;
        for (Point point : points) {
            if (point.x != leftExtremePointX) {
                double add = (point.x - leftExtremePointX) / currentWidth * diff;
                point.add(new Point((int)Math.round(add), 0));
            }
        }
        for (Hole hole : holes) {
            for (Point point : hole.getPoints()) {
                if (point.x != leftExtremePointX) {
                    double add = (point.x - leftExtremePointX) / currentWidth * diff;
                    point.add(new Point((int)Math.round(add), 0));
                }
            }
        }
    }
    public int getWidth() {
        return Math.abs(getExtremePoint(-1, 0).x - getExtremePoint(1, 0).x);
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

    public void merge(Surface s) {
        ArrayList<Double> fxPoints = new ArrayList<Double>();
        for (Point point : this.getPoints()) {
            fxPoints.add((double)point.x);
            fxPoints.add((double)point.y);
        }
        Double[] fpoints = fxPoints.toArray(new Double[fxPoints.size()]);
        javafx.scene.shape.Polygon firstPoly = new javafx.scene.shape.Polygon();
        firstPoly.getPoints().addAll(fpoints);

        ArrayList<Double> fxPoints2 = new ArrayList<Double>();
        for (Point point : s.getPoints()) {
            fxPoints2.add((double)point.x);
            fxPoints2.add((double)point.y);
        }
        javafx.scene.shape.Polygon secondPoly = new javafx.scene.shape.Polygon();
        secondPoly.getPoints().addAll(fxPoints2.toArray(new Double[fxPoints2.size()]));
        Path p3 = (Path)javafx.scene.shape.Polygon.union(firstPoly, secondPoly);
        Double[] pointsShape = new Double[(p3.getElements().size() - 1)*2];
        int i = 0;
        for(PathElement el : p3.getElements()){
            if(el instanceof MoveTo){
                MoveTo mt = (MoveTo) el;
                pointsShape[i] = mt.getX();
                pointsShape[i+1] = mt.getY();
            }
            if(el instanceof LineTo){
                LineTo lt = (LineTo) el;
                pointsShape[i] = lt.getX();
                pointsShape[i+1] = lt.getY();
            }
            i += 2;
        }

        javafx.scene.shape.Polygon newPolygon = new javafx.scene.shape.Polygon();
        newPolygon.getPoints().addAll(pointsShape);
        ArrayList<Point> newPoints = new ArrayList<Point>();
        for (i = 0; i < newPolygon.getPoints().size(); i += 2) {
            if (newPolygon.getPoints().get(i) == null) {
                return;
            }
            newPoints.add(new Point(newPolygon.getPoints().get(i).intValue(), newPolygon.getPoints().get(i + 1).intValue()));
        }
        points = newPoints;
        holes.addAll(s.getHoles());
        setPattern(getPattern());
    }

    public void movePatern(int x, int y) {
        if (pattern == null)
            return;
        pattern.movePatern(x, y);
        //pattern.build(material, this);
    }
}
