package fr.virtutuile.drawer;

import fr.virtutuile.domain.Point;
import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.Tile;
import fr.virtutuile.domain.VirtuTuileController;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class SurfacesDrawer {

    private final VirtuTuileController controller;

    public SurfacesDrawer(VirtuTuileController controller) {
        this.controller = controller;
    }

    public void drawPolygon(Graphics g, Surface surface) {
        Color color = new Color(surface.getColor().red, surface.getColor().green, surface.getColor().blue, surface.getColor().alpha);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(color);

        List<Integer> xPoly = new ArrayList<Integer>();
        List<Integer> yPoly = new ArrayList<Integer>();
        List<Point> points = surface.getPoints();
        for (Point point : points) {
            Point dest = controller.coordToGraphic(point.x, point.y);
            xPoly.add(dest.x);
            yPoly.add(dest.y);
        }
        for (int i = 0; i < points.size() - 2; i += 1) {
            Point   affPlace = controller.coordToGraphic((points.get(i).x + points.get(i + 1).x) / 2, (points.get(i).y + points.get(i + 1).y) / 2);

            double dis1 = Math.sqrt((points.get(i + 1).x - points.get(i).x) * (points.get(i + 1).x - points.get(i).x) + (points.get(i + 1).y - points.get(i).y) * (points.get(i + 1).y - points.get(i).y));
            g.drawString(String.valueOf(dis1), affPlace.x, affPlace.y);
        }
        Polygon polygon = new Polygon(xPoly.stream().mapToInt(i->i).toArray(), yPoly.stream().mapToInt(i->i).toArray(), xPoly.size());
        if (surface.isSelected()) {
            //taille de la ligne quand la surface est selectionnée
            g2.setStroke(new BasicStroke(2));
        } else {
            g2.setStroke(new BasicStroke(1));
        }
        g.drawPolygon(polygon);
        if (surface.getTiles().size() != 0) {
            for (Tile tile : surface.getTiles()) {
                List<Integer> xTilePoly = new ArrayList<Integer>();
                List<Integer> yTilePoly = new ArrayList<Integer>();
                List<Point> TilePoints = tile.getPoints();
                for (Point point : TilePoints) {
                    Point dest = controller.coordToGraphic(point.x, point.y);
                    xTilePoly.add(dest.x);
                    yTilePoly.add(dest.y);
                }
                Polygon polygonTile = new Polygon(xTilePoly.stream().mapToInt(i->i).toArray(), yTilePoly.stream().mapToInt(i->i).toArray(), xTilePoly.size());
                if (tile.isSelected()) {
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setStroke(new BasicStroke(1));
                }
                g.drawPolygon(polygonTile);
            }
        }

    }

    public void draw(Graphics g) {
        List<Surface> surfaces = controller.getSurfaces();
        for (Surface surface : surfaces) {
            this.drawPolygon(g, surface);
        }
    }
}
