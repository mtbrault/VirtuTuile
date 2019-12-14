package fr.virtutuile.view.panels;

import fr.virtutuile.domain.Point;
import fr.virtutuile.domain.SurfacesControllerObserver;
import fr.virtutuile.drawer.GridDrawer;
import fr.virtutuile.drawer.SurfacesDrawer;
import fr.virtutuile.view.frames.MainWindow;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class DrawingPanel extends JPanel implements SurfacesControllerObserver {

    private final   MainWindow mainWindow;

    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mainWindow.controller.onMouseMoved(new Point(e.getX(), e.getY()));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mainWindow.controller.onMouseMoved(new Point(e.getX(), e.getY()));
            }

        });
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                mainWindow.controller.onMousePressed(mainWindow.controller.graphicToCoord(me.getX(), me.getY()), SwingUtilities.isLeftMouseButton(me));

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point point = mainWindow.controller.graphicToCoord(e.getX(), e.getY());
                mainWindow.controller.onMouseReleased();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                int zoomSensibilty = 0;

                if (notches > zoomSensibilty) {
                    mainWindow.controller.zoomIn();

                }
                else if (notches < -zoomSensibilty) {
                    mainWindow.controller.zoomOut();
                }

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
        Point pos = new Point(getLocationOnScreen().x, getLocationOnScreen().y);
        mainWindow.controller.setCanvasPosition(pos);
        SurfacesDrawer surfacesDrawer = new SurfacesDrawer(mainWindow.controller);
        if (mainWindow.controller.getGridSwitch()) {
            GridDrawer gridDrawer = new GridDrawer(this.getSize().width, this.getSize().height, mainWindow);
            gridDrawer.draw(g);
        }
        surfacesDrawer.draw(g);
    }

    @Override
    public void notifyCreatedSurface() {
        repaint();
    }

}
