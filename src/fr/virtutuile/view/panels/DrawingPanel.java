package fr.virtutuile.view.panels;

import fr.virtutuile.domain.Point;
import fr.virtutuile.domain.SurfacesControllerObserver;
import fr.virtutuile.drawer.SurfacesDrawer;
import fr.virtutuile.view.frames.MainWindow;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel implements SurfacesControllerObserver {

    private final MainWindow mainWindow;

    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                System.out.println(me);
                Point point = mainWindow.controller.convertPoint(me.getX(), me.getY());
                mainWindow.controller.onMousePressed(point);

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Clicked");
                Point point = mainWindow.controller.convertPoint(e.getX(), e.getY());
                mainWindow.controller.onClick(point);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("Released");
                Point point = mainWindow.controller.convertPoint(e.getX(), e.getY());
                mainWindow.controller.onMouseReleased(point);
            }
        });
        mainWindow.controller.registerObserver(this);
        buildUp();
    }

    private void buildUp() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#E1E1E1"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        SurfacesDrawer surfacesDrawer = new SurfacesDrawer(mainWindow.controller);
        surfacesDrawer.draw(g);
    }

    @Override
    public void notifyCreatedSurface() {
        repaint();
    }

}
