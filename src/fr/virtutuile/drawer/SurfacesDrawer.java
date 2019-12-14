package fr.virtutuile.drawer;

import fr.virtutuile.domain.*;
import fr.virtutuile.domain.Point;

import java.awt.*;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

public class SurfacesDrawer {

    private final VirtuTuileController controller;

    public SurfacesDrawer(VirtuTuileController controller) {
        this.controller = controller;
    }

    /*public void drawHoles(Graphics2D g2, List<Hole> list) {
        for (fr.virtutuile.domain.Polygon hole : list) {
            List<Integer> xPoly = new ArrayList<Integer>();
            List<Integer> yPoly = new ArrayList<Integer>();
            List<Point> points = hole.getPoints();
            for (Point point : points) {
                Point graphicPoint = controller.coordToGraphic(point.x, point.y);
                xPoly.add(graphicPoint.x);
                yPoly.add(graphicPoint.y);
            }
            Polygon polygon = new Polygon(xPoly.stream().mapToInt(i->i).toArray(), yPoly.stream().mapToInt(i->i).toArray(), xPoly.size());
            g2.setColor(new Color(217, 217, 217));
            g2.fill(polygon);
            g2.draw(polygon);
        }
    }*/

    public void drawTiles(Graphics g, Graphics2D g2, List<Tile> tiles, String color) {
        for (Tile tile : tiles) {
            List<Integer> xTilePoly = new ArrayList<Integer>();
            List<Integer> yTilePoly = new ArrayList<Integer>();
            List<Point> TilePoints = tile.getPoints();
            for (Point point : TilePoints) {
                Point graphicPoint = controller.coordToGraphic(point.x, point.y);
                xTilePoly.add(graphicPoint.x);
                yTilePoly.add(graphicPoint.y);
            }
            Polygon polygonTile = new Polygon(xTilePoly.stream().mapToInt(i->i).toArray(), yTilePoly.stream().mapToInt(i->i).toArray(), xTilePoly.size());
            if (tile.isSelected()) {
                g2.setStroke(new BasicStroke(2));
            } else {
                g2.setStroke(new BasicStroke(1));
            }
            g2.setColor(Color.decode(color));
            g2.fill(polygonTile);
            g2.setColor(Color.BLACK);
            if (tile.isDetected())
                g2.setColor(Color.red);
            g.drawPolygon(polygonTile);
        }
    }

    public void drawPolygon(Graphics g, Surface surface) {
        Graphics2D g2 = (Graphics2D) g;

        List<Integer> xPoly = new ArrayList<Integer>();
        List<Integer> yPoly = new ArrayList<Integer>();
        List<Point> points = surface.getPoints();
        for (Point point : points) {
            Point graphicPoint = controller.coordToGraphic(point.x, point.y);
            xPoly.add(graphicPoint.x);
            yPoly.add(graphicPoint.y);
        }
        for (int i = 0; i < points.size() - 2; i += 1) {
            double dis1 = Math.sqrt((points.get(i + 1).x  - points.get(i).x) * (points.get(i + 1).x - points.get(i).x) + (points.get(i + 1).y - points.get(i).y) * (points.get(i + 1).y - points.get(i).y));
            Point graphicPoint = controller.coordToGraphic(((points.get(i).x  + points.get(i + 1).x)  / 2), (points.get(i).y + points.get(i + 1).y) / 2);
            g.drawString(String.valueOf(dis1), graphicPoint.x, graphicPoint.y);
        }
        Polygon polygon = new Polygon(xPoly.stream().mapToInt(i->i).toArray(), yPoly.stream().mapToInt(i->i).toArray(), xPoly.size());
        if (surface.getHoles().size() > 0) {
            Area awtShape = new Area(polygon);
            for (Hole hole : surface.getHoles()) {
                Area awtHole = Pattern.convertPolygonToShape(hole);
                awtShape.subtract(awtHole);
            }
            Polygon tmpPoly  = new Polygon();
            PathIterator iter = awtShape.getPathIterator(null);
            List<Point> tmpPoints = new ArrayList<>();
            double[] tmp = new double[2];
            while (!iter.isDone()) {
                iter.currentSegment(tmp);
                tmpPoly.addPoint((int)tmp[0], (int)tmp[1]);
                iter.next();
            }
            polygon = tmpPoly;
        }
        if (surface.isSelected()) {
            //taille de la ligne quand la surface est selectionnée
            g2.setStroke(new BasicStroke(2));
        } else {
            g2.setStroke(new BasicStroke(1));
        }
        g2.setColor(Color.decode(surface.getColor()));
        g2.fill(polygon);
        g2.setColor(Color.BLACK);
        g.drawPolygon(polygon);
        if (surface.getTiles().size() != 0) {
            drawTiles(g, g2, surface.getTiles(), surface.getMaterial().getColor());
        }
    }

    public void draw(Graphics g) {
        List<Surface> surfaces = controller.getSurfaces();
        for (Surface surface : surfaces) {
            this.drawPolygon(g, surface);
        }
    }
}
