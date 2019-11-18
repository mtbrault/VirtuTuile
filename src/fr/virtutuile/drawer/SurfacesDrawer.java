package fr.virtutuile.drawer;

import fr.virtutuile.domain.Point;
import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.VirtuTuileController;

import java.awt.*;
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
            xPoly.add(point.x - controller.camPos.x);
            yPoly.add(point.y - controller.camPos.y);
        }
        for (int i = 0; i < points.size() - 1; i += 1) {
            double dis1 = Math.sqrt((xPoly.get(i + 1)-xPoly.get(i))*(xPoly.get(i + 1)-xPoly.get(i)) + (yPoly.get(i + 1)-yPoly.get(i))*(yPoly.get(i + 1)-yPoly.get(i)));
            g.drawString(String.valueOf(dis1), (xPoly.get(i) + xPoly.get(i + 1)) / 2, (yPoly.get(i) + yPoly.get(i + 1)) / 2);
        }
        Polygon polygon = new Polygon(xPoly.stream().mapToInt(i->i).toArray(), yPoly.stream().mapToInt(i->i).toArray(), xPoly.size());
        if (surface.isSelected()) {
            //taille de la ligne quand la surface est selectionnée
            g2.setStroke(new BasicStroke(2));
        } else {
            g2.setStroke(new BasicStroke(1));
        }
        g.drawPolygon(polygon);
    }

    public void draw(Graphics g) {
        List<Surface> surfaces = controller.getSurfaces();
        for (Surface surface : surfaces) {
            this.drawPolygon(g, surface);
        }
    }
}
