package fr.virtutuile.view.panels;

import fr.virtutuile.domain.Material;
import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.ZoomControllerObserver;
import fr.virtutuile.view.frames.MainWindow;

import java.awt.*;

import javax.swing.*;

public class FooterPanel extends JPanel implements ZoomControllerObserver {

    private final   MainWindow mainWindow;
    private JLabel labelZoom;
    
    public FooterPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        buildUp();
        mainWindow.controller.registerObserverZoom(this);
        }

    private void buildUp() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Dimension footerDimension = this.getPreferredSize();
        footerDimension.height = 45;
        setPreferredSize(footerDimension);
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.black));
        labelZoom = new JLabel("Zoom : " + (int)(mainWindow.controller.getZoom() * 100) + "%");
        add(labelZoom);
    }
    
    
    @Override
    public void notifyUpdatedZoom() {
    	labelZoom.setText("Zoom : " + (int)(mainWindow.controller.getZoom() * 100) + "%");
    	labelZoom.repaint();
        repaint();
    }
}
