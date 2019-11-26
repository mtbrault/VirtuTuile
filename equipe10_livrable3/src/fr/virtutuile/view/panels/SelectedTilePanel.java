package fr.virtutuile.view.panels;

import fr.virtutuile.domain.VirtuTuileController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SelectedTilePanel extends JPanel {
    private VirtuTuileController controller;
    SelectedTilePanel(VirtuTuileController controller) {
        this.controller = controller;
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setPreferredSize(new Dimension(350, 200));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (controller.getSelectedTile() != null) {
            java.util.List<Integer> xPoly = new ArrayList<Integer>();
            java.util.List<Integer> yPoly = new ArrayList<Integer>();
            List<fr.virtutuile.domain.Point> points = controller.getSelectedTile().getPoints();
            xPoly.add(125);
            yPoly.add(50);
            int facteur = 3;
            /* Piste de code si l'on veut étendre la tile dans le panel
            int max = 0;
            for (int i = 0; i < points.size() - 1; i += 1) {
                int val = points.get(i + 1).y - points.get(i).y;
                if (val > max)
                    max = val;
            }
            if (max != 0)
                facteur = 350 / max;*/
            for (int i = 0; i < points.size() - 1; i += 1) {
                int distX;
                int distY;
                distX = points.get(i + 1).x - points.get(i).x;
                distY = points.get(i + 1).y - points.get(i).y;

                double dis1 = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
                xPoly.add(xPoly.get(i) + distX * facteur);
                yPoly.add(yPoly.get(i) + distY * facteur);
                g.drawString(String.valueOf(dis1), (xPoly.get(i)  + xPoly.get(i + 1))  / 2, (yPoly.get(i) + yPoly.get(i + 1)) / 2);
            }
            Polygon polygon = new Polygon(xPoly.stream().mapToInt(x->x).toArray(), yPoly.stream().mapToInt(y->y).toArray(), xPoly.size());
            g.drawPolygon(polygon);
        }
    }
}