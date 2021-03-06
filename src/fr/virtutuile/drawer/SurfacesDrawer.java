package fr.virtutuile.drawer;

import fr.virtutuile.domain.*;
import fr.virtutuile.domain.Point;
import fr.virtutuile.domain.Polygon;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

public class SurfacesDrawer {

    private final VirtuTuileController controller;

    public SurfacesDrawer(VirtuTuileController controller) {
        this.controller = controller;
    }

    public void drawTiles(Graphics g, Graphics2D g2, List<Tile> tiles, Color color) {
        for (Tile tile : tiles) {
            List<Integer> xTilePoly = new ArrayList<Integer>();
            List<Integer> yTilePoly = new ArrayList<Integer>();
            List<Point> TilePoints = tile.getPoints();
            for (Point point : TilePoints) {
                Point graphicPoint = controller.coordToGraphic(point.x, point.y);
                xTilePoly.add(graphicPoint.x);
                yTilePoly.add(graphicPoint.y);
            }
            java.awt.Polygon polygonTile = new java.awt.Polygon(xTilePoly.stream().mapToInt(i->i).toArray(), yTilePoly.stream().mapToInt(i->i).toArray(), xTilePoly.size());
            if (tile.isSelected()) {
                g2.setStroke(new BasicStroke(2));
            } else {
                g2.setStroke(new BasicStroke(1));
            }
            g2.setColor(color);
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
        java.awt.Polygon polygon = new java.awt.Polygon(xPoly.stream().mapToInt(i->i).toArray(), yPoly.stream().mapToInt(i->i).toArray(), xPoly.size());
        Area awtShape = new Area(polygon);
        if (surface.getHoles().size() > 0) {
            for (Hole hole : surface.getHoles()) {
                List<Point> list = new ArrayList<>();
                for (Point p : hole.getPoints()) {
                    list.add(controller.coordToGraphic(p.x, p.y));
                }
                Polygon toDraw = new Polygon(list, PolygonType.HOLE);
                Area awtHole = Pattern.convertPolygonToShape(toDraw);
                awtShape.subtract(awtHole);
            }
        }
        if (surface.isSelected()) {
            //taille de la ligne quand la surface est selectionnée
            g2.setStroke(new BasicStroke(2));
        } else {
            g2.setStroke(new BasicStroke(1));
        }
        g2.setColor(surface.getColor());
        g2.fill(awtShape);
        g2.setColor(Color.BLACK);
        g2.draw(awtShape);
        if (surface.getTiles().size() != 0) {
            drawTiles(g, g2, surface.getTiles(), surface.getMaterial().getColor());
        }
        for (Line2D line : controller.getLines()) {
            ((Graphics2D) g).draw(line);
            double dis1 = Math.sqrt(Math.pow(line.getX2() - line.getX1(), 2) + Math.pow(line.getY2() - line.getY1(), 2));
            g.drawString(String.valueOf(controller.convertMeteringToDisplay((int)Math.round(dis1))), (int)((line.getX2() + line.getX1()) / 2), (int)((line.getY2() + line.getY1()) / 2));
        }
        for (int i = 0; i < points.size() - 1; i += 1) {
            double dis1 = Math.sqrt((points.get(i + 1).x  - points.get(i).x) * (points.get(i + 1).x - points.get(i).x) + (points.get(i + 1).y - points.get(i).y) * (points.get(i + 1).y - points.get(i).y));
            Point graphicPoint = controller.coordToGraphic(((points.get(i).x  + points.get(i + 1).x)  / 2), (points.get(i).y + points.get(i + 1).y) / 2);
            g.drawString(String.valueOf(controller.convertMeteringToDisplay((int)Math.round(dis1))), graphicPoint.x, graphicPoint.y);
        }
    }

    public void draw(Graphics g) {
        List<Surface> surfaces = controller.getSurfaces();
        for (Surface surface : surfaces) {
            this.drawPolygon(g, surface);
        }
    }
}
