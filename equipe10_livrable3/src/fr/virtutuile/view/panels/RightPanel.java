package fr.virtutuile.view.panels;

import fr.virtutuile.view.frames.MainWindow;

import java.awt.*;

import javax.swing.*;

public class RightPanel extends JPanel {

    public RightPanel(MainWindow outer) {
        buildUp();
    }

    private void buildUp() {
        setLayout(new BorderLayout());
    }
}
