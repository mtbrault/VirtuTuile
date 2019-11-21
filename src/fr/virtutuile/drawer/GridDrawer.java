package fr.virtutuile.drawer;
import fr.virtutuile.domain.VirtuTuileController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class GridDrawer {
    int height;
    int width;

    public GridDrawer(int w, int h) {
        height = h;
        width = w;
    }

    public void draw(Graphics g) {
        //System.out.printLn("helloprint");
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 100 ; i < width ; i += 100) {
            Line2D line = new Line2D.Float(i, 0, i, height);
            g2.draw(line);
        }
        for (int i = 100 ; i < height ; i += 100) {
            Line2D line = new Line2D.Float(0, i, width, i);
            g2.draw(line);
        }
    }
}
