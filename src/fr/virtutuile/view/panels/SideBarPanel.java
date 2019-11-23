package fr.virtutuile.view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.SurfacesControllerObserver;
import fr.virtutuile.view.frames.MainWindow;

public class SideBarPanel extends JPanel implements SurfacesControllerObserver {
    private MainWindow mainWindow;
    private JPanel panel_2;

    public SideBarPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.panel_2 = new JPanel();
        buildUp();
        mainWindow.controller.registerObserver(this);

    }

    private void buildUp() {
        setLayout(new GridLayout(2, 1, 0, 0));
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(350, 900));
        setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setPreferredSize(new Dimension(350, 200));
        add(panel);

        JLabel lblNewLabel_1 = new JLabel("Découpage de la première tuile");
        lblNewLabel_1.setVerticalAlignment(SwingConstants.BOTTOM);
        panel.add(lblNewLabel_1);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.white);
        panel_1.setBorder(BorderFactory.createLineBorder(Color.black));
        add(panel_1);
        panel_1.setPreferredSize(new Dimension(350, 900));
        panel_1.setLayout(new GridLayout(0, 1, 0, 0));

        panel_2.setLayout(new GridLayout(0, 1, 0, 0));
        JScrollPane scrollPane = new JScrollPane(panel_2, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel_1.add(scrollPane, panel_2);
        JPanel panel_5 = new JPanel();

        panel_2.add(panel_5);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void notifyCreatedSurface() {

        int nbSurface = 0;
        panel_2.removeAll();
        panel_2.updateUI();
        for (Surface surface : mainWindow.controller.getSurfaces()) {
            nbSurface++;
            panel_2.add(new SideBarPanelSurface(surface, nbSurface, mainWindow.controller));
        }
        panel_2.repaint();

        repaint();
    }
}