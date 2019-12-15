package fr.virtutuile.domain;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Pattern implements java.io.Serializable {
    public Pattern() {
    }

    private Tile getIntersection(Surface firstSurface, Tile secondSurface) {
        ArrayList<Double> fxPoints = new ArrayList<Double>();
        for (Point point : firstSurface.getPoints()) {
            fxPoints.add((double)point.x);
            fxPoints.add((double)point.y);
        }
        Double[] fpoints = fxPoints.toArray(new Double[fxPoints.size()]);
        javafx.scene.shape.Polygon firstPoly = new javafx.scene.shape.Polygon();
        firstPoly.getPoints().addAll(fpoints);

        ArrayList<Double> fxPoints2 = new ArrayList<Double>();
        for (Point point : secondSurface.getPoints()) {
            fxPoints2.add((double)point.x);
            fxPoints2.add((double)point.y);
        }
        javafx.scene.shape.Polygon secondPoly = new javafx.scene.shape.Polygon();
        secondPoly.getPoints().addAll(fxPoints2.toArray(new Double[fxPoints2.size()]));
        Path p3 = (Path)javafx.scene.shape.Polygon.intersect(firstPoly, secondPoly);
        if (p3 == null || p3.getElements().size() == 0) {
            return null;
        }
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
                return null;
            }
            newPoints.add(new Point(newPolygon.getPoints().get(i).intValue(), newPolygon.getPoints().get(i + 1).intValue()));
        }
       return new Tile(newPoints);
    }

    public static Area convertPolygonToShape(Polygon p) {
        int xtab[] = new int[p.getPoints().size()];
        int ytab[] = new int[p.getPoints().size()];
        int i = 0;
        for (Point a : p.getPoints()) {
            xtab[i] = a.x;
            ytab[i] = a.y;
            i += 1;
        }
        java.awt.Polygon awtPoly = new java.awt.Polygon(xtab, ytab, p.getPoints().size());
        return new Area(awtPoly);
    }

    public static Polygon convertShapeToPolygon(Area s, PolygonType type) {
        PathIterator iter = s.getPathIterator(null);
        List<Point> points = new ArrayList<>();
        double[] tmp = new double[2];
        while (!iter.isDone()) {
            iter.currentSegment(tmp);
            int x = (int)tmp[0];
            int y = (int)tmp[1];
            points.add(new Point(x, y));
            iter.next();
        }
        if (type == PolygonType.TILE)
            return new Tile(points);
        else if (type == PolygonType.HOLE)
            return new Hole(points);
        else if (type == PolygonType.SURFACE)
            return new Surface(points);
        return null;
    }

    public List<Tile> holeManager(Surface surface, List<Tile> tiles) {
        List<Tile> newList = new ArrayList<>(tiles);
        for (Tile tile : tiles) {
            for (Hole hole : surface.getHoles()) {
                int inside = 0;
                for (Point p : tile.getPoints()) {
                    if (hole.isInside(p))
                        inside += 1;
                }
                if (inside == 0) {

                } else if (inside < tile.getPoints().size()) {
                    newList.remove(tile);
                    Area awtHole = convertPolygonToShape(hole);
                    Area awtTile = convertPolygonToShape(tile);
                    awtTile.subtract(awtHole);
                    newList.add((Tile)convertShapeToPolygon(awtTile, PolygonType.TILE));
                } else if (inside == tile.getPoints().size()){
                    newList.remove(tile);
                }
            }
        }
        return (newList);
    }

    public List<Tile> build(Material material, Surface surface) {
        List<Tile> tiles = new ArrayList<Tile>();
        Surface extremeSurface = surface.getExtremeSurface();
        Point point1 = extremeSurface.getPoints().get(0);
        Point point2 = extremeSurface.getPoints().get(1);
        Point point4 = extremeSurface.getPoints().get(3);
        int minPointX = point1.x;
        int minPointY = point1.y;
        if (material == null)
            return new ArrayList<>();
        int tileHeight = material.getHeight();
        int tileWidth = material.getWidth();
        if (surface.isVertical()) {
            tileHeight = material.getWidth();
            tileWidth = material.getHeight();
        }
        int nbXTiles = Math.abs(point1.x - point2.x) / (tileWidth + surface.getJointSize()) + 1;
        int nbYTiles = Math.abs(point1.y - point4.y) / (tileHeight + surface.getJointSize()) + 1;
        if (surface.getPatternId() == 0) {
            for (int y = 0; y < nbYTiles; y++) {
                for (int x = 0; x < nbXTiles; x++) {
                    ArrayList points = new ArrayList<Point>();
                    int Xpoint0 = x * tileWidth + minPointX + (surface.getJointSize() * x);
                    if (y % 2 == 0) {
                        Xpoint0 -= tileWidth * surface.getTileShift();
                        if (Xpoint0 < point1.x) {
                            Xpoint0 = point1.x;
                        }
                    }
                    int Ypoint0 = y *tileHeight + minPointY + (y*surface.getJointSize());
                    points.add(new Point(Xpoint0, Ypoint0));

                    int Xpoint = (x + 1) * tileWidth + minPointX + (x*surface.getJointSize());
                    if (y % 2 == 0) {
                        Xpoint -= tileWidth * surface.getTileShift();
                    }
                    points.add(new Point(Xpoint, Ypoint0));

                    int Ypoint = (y + 1) * tileHeight + minPointY + (y*surface.getJointSize());
                    points.add(new Point(Xpoint, Ypoint));

                    points.add(new Point(Xpoint0, Ypoint));
                    Tile tile = this.getIntersection(surface, new Tile(points));
                    if (tile != null) {
                        tiles.add(tile);
                    }
                }
            }
        } else if (surface.getPatternId() == 2) {
            for (int y = 0; y < nbYTiles / 2 + 1; y++) {
                for (int x = 0; x < nbXTiles; x++) {
                    ArrayList points = new ArrayList<Point>();
                    ArrayList secondPoints = new ArrayList<Point>();
                    int maxSize = Math.max(tileWidth, tileHeight);
                    int minSize = Math.min(tileWidth, tileHeight);
                    int Xpoint0 = x * maxSize + minPointX + (surface.getJointSize() * x);
                    int Ypoint0 = y * maxSize + minPointY + (y*surface.getJointSize());
                    if (x % 2 == y % 2) {
                        int Xpoint1 = Xpoint0 + maxSize;
                        int Ypoint1 = Ypoint0 + minSize;
                        int Ypoint2 = Ypoint0 + (2 * minSize);
                        points.add(new Point(Xpoint0, Ypoint0));
                        points.add(new Point(Xpoint1, Ypoint0));
                        points.add(new Point(Xpoint1, Ypoint1));
                        points.add(new Point(Xpoint0, Ypoint1));

                        secondPoints.add(new Point(Xpoint0, Ypoint1));
                        secondPoints.add(new Point(Xpoint1, Ypoint1));
                        secondPoints.add(new Point(Xpoint1, Ypoint2));
                        secondPoints.add(new Point(Xpoint0, Ypoint2));

                    } else {
                        int Xpoint1 = Xpoint0 + minSize;
                        int Ypoint1 = Ypoint0 + maxSize;
                        int Xpoint2 = Xpoint0 + 2 * minSize;
                        points.add(new Point(Xpoint0, Ypoint0));
                        points.add(new Point(Xpoint1, Ypoint0));
                        points.add(new Point(Xpoint1, Ypoint1));
                        points.add(new Point(Xpoint0, Ypoint1));

                        secondPoints.add(new Point(Xpoint1, Ypoint0));
                        secondPoints.add(new Point(Xpoint2, Ypoint0));
                        secondPoints.add(new Point(Xpoint2, Ypoint1));
                        secondPoints.add(new Point(Xpoint1, Ypoint1));
                    }
                    Tile tile = this.getIntersection(surface, new Tile(points));
                    if (tile != null) {
                        tiles.add(tile);
                    }
                    Tile tile2 = this.getIntersection(surface, new Tile(secondPoints));
                    if (tile2 != null) {
                        tiles.add(tile2);
                    }
                }
            }
        } else if (surface.getPatternId() == 1) {
            for (int y = 0; y < nbYTiles; y++) {
                for (int x = 0; x < nbXTiles; x++) {
                    ArrayList points = new ArrayList<Point>();
                    Point center = new Point(minPointX + x * tileWidth + (tileWidth / 2) + (surface.getJointSize() * x), minPointY + y * tileHeight + (tileHeight / 2) + (y*surface.getJointSize()));
                    int diagonalSize = (int)Math.sqrt(Math.pow(tileWidth, 2) + Math.pow(tileHeight, 2));
                    int Xpoint0 = x * tileWidth + minPointX + (surface.getJointSize() * x) - center.x;
                    int Ypoint0 = y * tileHeight + minPointY + (y*surface.getJointSize())  - center.y;
                    int Xpoint1 = (x + 1) * tileWidth + minPointX + (surface.getJointSize() * x) - center.x;
                    int Ypoint1 = y * tileHeight + minPointY + (y*surface.getJointSize())  - center.y;
                    int Xpoint2 = (x + 1) * tileWidth + minPointX + (x*surface.getJointSize()) - center.x;
                    int Ypoint2 = (y + 1) * tileHeight + minPointY + (y*surface.getJointSize()) - center.y;
                    int Xpoint3 = x * tileWidth + minPointX + (x*surface.getJointSize()) - center.x;
                    int Ypoint3 = (y + 1) * tileHeight + minPointY + (y*surface.getJointSize()) - center.y;
                    int angle = -45;
                    int pointx0 = (int)(Xpoint0 * Math.cos(Math.toRadians(angle)) - Ypoint0 * Math.sin(Math.toRadians(angle)));
                    int pointy0 = (int)(Xpoint0 * Math.sin(Math.toRadians(angle)) + Ypoint0 * Math.cos(Math.toRadians(angle)));
                    int pointx1 = (int)(Xpoint1 * Math.cos(Math.toRadians(angle)) - Ypoint1 * Math.sin(Math.toRadians(angle)));
                    int pointy1 = (int)(Xpoint1 * Math.sin(Math.toRadians(angle)) + Ypoint1 * Math.cos(Math.toRadians(angle)));
                    int pointx2 = (int)(Xpoint2 * Math.cos(Math.toRadians(angle)) - Ypoint2 * Math.sin(Math.toRadians(angle)));
                    int pointy2 = (int)(Xpoint2 * Math.sin(Math.toRadians(angle)) + Ypoint2 * Math.cos(Math.toRadians(angle)));
                    int pointx3 = (int)(Xpoint3 * Math.cos(Math.toRadians(angle)) - Ypoint3 * Math.sin(Math.toRadians(angle)));
                    int pointy3 = (int)(Xpoint3 * Math.sin(Math.toRadians(angle)) + Ypoint3 * Math.cos(Math.toRadians(angle)));
                    int shiftX =  (diagonalSize - tileWidth) * x;
                    int shiftY =  (diagonalSize - tileHeight) * y;
                    points.add(new Point(pointx0 + center.x + shiftX, pointy0 + center.y + shiftY));
                    points.add(new Point(pointx1 + center.x + shiftX, pointy1 + center.y + shiftY));
                    points.add(new Point(pointx2 + center.x + shiftX, pointy2 + center.y + shiftY));
                    points.add(new Point(pointx3 + center.x + shiftX, pointy3 + center.y + shiftY));
                    Tile tile = this.getIntersection(surface, new Tile(points));
                    if (tile != null) {
                        tiles.add(tile);
                    }
                }
            }
            int diagonalSize = (int)Math.sqrt(Math.pow(tileWidth, 2) + Math.pow(tileHeight, 2));
            minPointY = minPointY -  diagonalSize / 2 - surface.getJointSize() / 2;
            minPointX = minPointX - diagonalSize / 2 - surface.getJointSize() / 2;
            for (int y = 0; y < nbYTiles; y++) {
                for (int x = 0; x < nbXTiles; x++) {
                    ArrayList points = new ArrayList<Point>();
                    Point center = new Point(minPointX + x * tileWidth + (tileWidth / 2) + (surface.getJointSize() * x), minPointY + y * tileHeight + (tileHeight / 2) + (y*surface.getJointSize()));
                    int Xpoint0 = x * tileWidth + minPointX + (surface.getJointSize() * x) - center.x;
                    int Ypoint0 = y * tileHeight + minPointY + (y*surface.getJointSize())  - center.y;
                    int Xpoint1 = (x + 1) * tileWidth + minPointX + (surface.getJointSize() * x) - center.x;
                    int Ypoint1 = y * tileHeight + minPointY + (y*surface.getJointSize())  - center.y;
                    int Xpoint2 = (x + 1) * tileWidth + minPointX + (x*surface.getJointSize()) - center.x;
                    int Ypoint2 = (y + 1) * tileHeight + minPointY + (y*surface.getJointSize()) - center.y;
                    int Xpoint3 = x * tileWidth + minPointX + (x*surface.getJointSize()) - center.x;
                    int Ypoint3 = (y + 1) * tileHeight + minPointY + (y*surface.getJointSize()) - center.y;
                    int angle = -45;
                    int pointx0 = (int)(Xpoint0 * Math.cos(Math.toRadians(angle)) - Ypoint0 * Math.sin(Math.toRadians(angle)));
                    int pointy0 = (int)(Xpoint0 * Math.sin(Math.toRadians(angle)) + Ypoint0 * Math.cos(Math.toRadians(angle)));
                    int pointx1 = (int)(Xpoint1 * Math.cos(Math.toRadians(angle)) - Ypoint1 * Math.sin(Math.toRadians(angle)));
                    int pointy1 = (int)(Xpoint1 * Math.sin(Math.toRadians(angle)) + Ypoint1 * Math.cos(Math.toRadians(angle)));
                    int pointx2 = (int)(Xpoint2 * Math.cos(Math.toRadians(angle)) - Ypoint2 * Math.sin(Math.toRadians(angle)));
                    int pointy2 = (int)(Xpoint2 * Math.sin(Math.toRadians(angle)) + Ypoint2 * Math.cos(Math.toRadians(angle)));
                    int pointx3 = (int)(Xpoint3 * Math.cos(Math.toRadians(angle)) - Ypoint3 * Math.sin(Math.toRadians(angle)));
                    int pointy3 = (int)(Xpoint3 * Math.sin(Math.toRadians(angle)) + Ypoint3 * Math.cos(Math.toRadians(angle)));
                    int shiftX =  (diagonalSize - tileWidth) * x;
                    int shiftY =  (diagonalSize - tileHeight) * y;
                    points.add(new Point(pointx0 + center.x + shiftX, pointy0 + center.y + shiftY));
                    points.add(new Point(pointx1 + center.x + shiftX, pointy1 + center.y + shiftY));
                    points.add(new Point(pointx2 + center.x + shiftX, pointy2 + center.y + shiftY));
                    points.add(new Point(pointx3 + center.x + shiftX, pointy3 + center.y + shiftY));
                    Tile tile = this.getIntersection(surface, new Tile(points));
                    if (tile != null) {
                        tiles.add(tile);
                    }
                }
            }
        }
        if (surface.getHoles().size() > 0)
            tiles = holeManager(surface, tiles);
        return tiles;
    }
}
