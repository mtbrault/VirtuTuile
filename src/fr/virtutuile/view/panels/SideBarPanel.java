package fr.virtutuile.view.panels;

import fr.virtutuile.view.frames.MainWindow;

import java.awt.*;

import javax.swing.*;

public class SideBarPanel extends JPanel {

    public SideBarPanel(MainWindow outer) {
        buildUp();
    }

    private void buildUp() {
        setLayout(new GridLayout(2, 1, 0, 0));
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(325, 200));
        setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        add(panel);

        JLabel lblNewLabel_1 = new JLabel("Découpage de la première tuile");
        lblNewLabel_1.setVerticalAlignment(SwingConstants.BOTTOM);
        panel.add(lblNewLabel_1);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.white);
        panel_1.setBorder(BorderFactory.createLineBorder(Color.black));
        add(panel_1);

        JScrollPane scrollPane = new JScrollPane();
        panel_1.add(scrollPane);

        JLabel lblNewLabel = new JLabel("Listes des surfaces");
        panel_1.add(lblNewLabel);
    }
}
