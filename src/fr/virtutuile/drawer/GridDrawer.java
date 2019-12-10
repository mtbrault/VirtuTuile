package fr.virtutuile.drawer;

import java.awt.*;
import java.awt.geom.Line2D;

public class GridDrawer {
    private int height;
    private int width;
    private int size = 100;

    public GridDrawer(int w, int h) {
        height = h;
        width = w;
    }

    public void setSize(int value) {
       size = value;
    }

    public void drawDashedLine(Graphics2D g2, Line2D line) {
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2.setStroke(dashed);
        g2.draw(line);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (int i = size ; i < width ; i += size) {
            Line2D line = new Line2D.Float(i, 0, i, height);
            drawDashedLine(g2, line);
        }
        for (int i = size ; i < height ; i += size) {
            Line2D line = new Line2D.Float(0, i, width, i);
            drawDashedLine(g2, line);
        }
    }
}
