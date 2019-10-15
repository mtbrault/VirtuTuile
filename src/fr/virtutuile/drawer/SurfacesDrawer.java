package fr.virtutuile.drawer;

import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.VirtuTuileController;

import java.awt.Graphics;
import java.util.List;
import java.awt.Color;
import java.awt.Polygon;

public class SurfacesDrawer {

    private final VirtuTuileController controller;

    public SurfacesDrawer(VirtuTuileController controller) {
        this.controller = controller;
    }

    public void drawPolygon(Graphics g, Surface surface) {
        Color color = new Color(surface.getColor().red, surface.getColor().green, surface.getColor().blue, surface.getColor().alpha);
        g.setColor(color);
        int xPoly[] = {150, 250, 325, 375, 450, 275, 100};
        int yPoly[] = {150, 100, 125, 225, 250, 375, 300};

        Polygon polygon = new Polygon(xPoly, yPoly, xPoly.length);

        g.drawPolygon(polygon);
    }

    public void draw(Graphics g) {
        List<Surface> surfaces = controller.getSurfaces();
        for (Surface surface : surfaces) {
            this.drawPolygon(g, surface);
        }
    }
}
