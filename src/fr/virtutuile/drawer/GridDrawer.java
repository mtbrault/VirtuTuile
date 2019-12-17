package fr.virtutuile.drawer;

import fr.virtutuile.domain.Point;
import fr.virtutuile.view.frames.MainWindow;

import java.awt.*;
import java.awt.geom.Line2D;

public class GridDrawer {
    private int height;
    private int width;
    private final MainWindow mainWindow;

    public GridDrawer(int w, int h, MainWindow mainW) {
        height = h;
        width = w;
        mainWindow = mainW;
    }

    public void drawDashedLine(Graphics2D g2, Line2D line) {
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2.setStroke(dashed);
        g2.draw(line);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int dim = mainWindow.controller.getGridDim();
        double zoom = mainWindow.controller.getZoom();
        int dimSizeInGraphic = (int)Math.round(dim * zoom);
        Point xyStart = mainWindow.controller.graphicToCoord(0, 0);
        xyStart.x = xyStart.x - xyStart.x % dim + dim;
        xyStart.y = xyStart.y - xyStart.y % dim + dim;
        xyStart = mainWindow.controller.coordToGraphic(xyStart.x, xyStart.y);

        if (dimSizeInGraphic < 3) {
            System.out.println("Zoom too big to show grid.");
            return;
        }
        for (int i = xyStart.x ; i < width ; i += dimSizeInGraphic) {
            Line2D line = new Line2D.Float(i, 0, i, height);
            drawDashedLine(g2, line);
        }
        for (int i = xyStart.y ; i < height ; i += dimSizeInGraphic) {
            Line2D line = new Line2D.Float(0, i, width, i);
            drawDashedLine(g2, line);
        }

    }
}
