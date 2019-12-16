package fr.virtutuile.view.panels;

import fr.virtutuile.domain.Tile;
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
            java.util.List<Double> xPoly = new ArrayList<Double>();
            java.util.List<Double> yPoly = new ArrayList<Double>();
            Tile selectedTile = controller.getSelectedTile();
            double distX = Math.abs(selectedTile.getExtremePoint(-1, 0).x - selectedTile.getExtremePoint(1, 0).x);
            double distY = Math.abs(selectedTile.getExtremePoint(0, 1).y - selectedTile.getExtremePoint(0, -1).y);
            double maxDist = Math.max(distX, distY);
            List<fr.virtutuile.domain.Point> points = selectedTile.getPoints();
            xPoly.add(125.0);
            yPoly.add(50.0);
            for (int i = 0; i < points.size() - 1; i += 1) {
                distX = (points.get(i + 1).x - points.get(i).x) / maxDist * 100;
                distY = (points.get(i + 1).y - points.get(i).y) / maxDist * 100;
                double dis1 = Math.sqrt(Math.pow(points.get(i + 1).x - points.get(i).x, 2) + Math.pow(points.get(i + 1).y - points.get(i).y, 2));
                g.drawString(String.valueOf(Math.round(dis1)), (int)(xPoly.get(i) + distX / 2), (int)(yPoly.get(i) + distY / 2));
                xPoly.add(xPoly.get(i) + distX);
                yPoly.add(yPoly.get(i) + distY);
            }
            Polygon polygon = new Polygon(xPoly.stream().mapToInt(x->x.intValue()).toArray(), yPoly.stream().mapToInt(y->y.intValue()).toArray(), xPoly.size());
            g.drawPolygon(polygon);
        }
    }
}
