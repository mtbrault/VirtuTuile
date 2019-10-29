package fr.virtutuile.view.panels;

import fr.virtutuile.view.frames.MainWindow;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class CenterPanel extends JPanel {

    public CenterPanel(MainWindow outer) {
        buildUp();
    }

    private void buildUp() {
        setLayout(new BorderLayout());
    }
}
