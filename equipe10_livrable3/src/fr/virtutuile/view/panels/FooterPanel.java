package fr.virtutuile.view.panels;

import fr.virtutuile.view.frames.MainWindow;

import java.awt.*;

import javax.swing.*;

public class FooterPanel extends JPanel {

    private final   MainWindow mainWindow;

    public FooterPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        buildUp();
    }

    private void buildUp() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Dimension footerDimension = this.getPreferredSize();
        footerDimension.height = 45;
        setPreferredSize(footerDimension);
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel lblNewLabel_2 = new JLabel("Zoom : " + (mainWindow.controller.getZoom() * 100));
        add(lblNewLabel_2);
    }
}
