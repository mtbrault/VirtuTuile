package fr.virtutuile.view.panels;

import fr.virtutuile.view.frames.MainWindow;

import java.awt.*;

import javax.swing.*;

public class FooterPanel extends JPanel {

    public FooterPanel(MainWindow outer) {
        buildUp();
    }

    private void buildUp() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Dimension footerDimension = this.getPreferredSize();
        footerDimension.height = 45;
        setPreferredSize(footerDimension);
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel lblNewLabel_2 = new JLabel("Zoom : ");
        add(lblNewLabel_2);
    }
}
