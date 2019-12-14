package fr.virtutuile.domain;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

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
    public List<Tile> build(Material material, Surface surface) {
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        Surface extremeSurface = surface.getExtremeSurface();
        Point point1 = extremeSurface.getPoints().get(0);
        Point point2 = extremeSurface.getPoints().get(1);
        Point point4 = extremeSurface.getPoints().get(3);
        int minPointX = point1.x;
        int minPointY = point1.y;
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
        }
        return tiles;
    }
}
