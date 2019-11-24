package fr.virtutuile.view.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.VirtuTuileController;

import javax.swing.SwingConstants;

public class SideBarPanelSurface extends JPanel {
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField;

    public SideBarPanelSurface(Surface surface, int nbSurface, VirtuTuileController controller) {
        JPanel panel_6 = new JPanel();
        add(panel_6);
        panel_6.setBackground(Color.WHITE);
        GridBagLayout gbl_panel_6 = new GridBagLayout();
        gbl_panel_6.columnWidths = new int[] { 139, 206, 0 };
        gbl_panel_6.rowHeights = new int[] { 18, 0, 0, 0, 0, 0 };
        gbl_panel_6.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel_6.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        panel_6.setLayout(gbl_panel_6);
        JLabel lblSurfaceX = new JLabel("Surface " + nbSurface);
        lblSurfaceX.setFont(new Font("Dialog", Font.BOLD, 15));
        GridBagConstraints gbc_lblSurfaceX = new GridBagConstraints();
        gbc_lblSurfaceX.insets = new Insets(0, 0, 5, 5);
        gbc_lblSurfaceX.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblSurfaceX.gridx = 0;
        gbc_lblSurfaceX.gridy = 0;
        panel_6.add(lblSurfaceX, gbc_lblSurfaceX);

        JLabel lblHauteur = new JLabel("Largeur");
        lblHauteur.setHorizontalAlignment(SwingConstants.LEFT);
        lblHauteur.setVerticalAlignment(SwingConstants.BOTTOM);
        GridBagConstraints gbc_lblHauteur = new GridBagConstraints();
        gbc_lblHauteur.anchor = GridBagConstraints.WEST;
        gbc_lblHauteur.insets = new Insets(0, 0, 5, 5);
        gbc_lblHauteur.gridx = 0;
        gbc_lblHauteur.gridy = 1;
        panel_6.add(lblHauteur, gbc_lblHauteur);

        textField = new JTextField("" + surface.getWidth());
        textField.setColumns(10);
        textField.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        surface.setWidth(Integer.parseInt(textField.getText()));
                        surface.onMoved();
                        controller.notifyObserverForSurfaces();
                    }
                }
        );

        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 0);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 1;
        panel_6.add(textField, gbc_textField);

        JLabel lblNewLabel = new JLabel("Hauteur");
        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 2;
        panel_6.add(lblNewLabel, gbc_lblNewLabel);

        textField_1 = new JTextField("" + surface.getHeight());
        textField_1.setColumns(10);
        textField_1.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        surface.setHeight(Integer.parseInt(textField_1.getText()));
                        surface.onMoved();
                        controller.notifyObserverForSurfaces();
                    }
        });
        GridBagConstraints gbc_textField_1 = new GridBagConstraints();
        gbc_textField_1.insets = new Insets(0, 0, 5, 0);
        gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_1.gridx = 1;
        gbc_textField_1.gridy = 2;
        panel_6.add(textField_1, gbc_textField_1);
        textField_1.setColumns(10);

        JLabel lblEpaisseurDuJoin = new JLabel("Epaisseur du joint");
        lblEpaisseurDuJoin.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gbc_lblEpaisseurDuJoin = new GridBagConstraints();
        gbc_lblEpaisseurDuJoin.anchor = GridBagConstraints.WEST;
        gbc_lblEpaisseurDuJoin.insets = new Insets(0, 0, 5, 5);
        gbc_lblEpaisseurDuJoin.gridx = 0;
        gbc_lblEpaisseurDuJoin.gridy = 3;
        panel_6.add(lblEpaisseurDuJoin, gbc_lblEpaisseurDuJoin);

        textField_2 = new JTextField();
        GridBagConstraints gbc_textField_2 = new GridBagConstraints();
        gbc_textField_2.insets = new Insets(0, 0, 5, 0);
        gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_2.gridx = 1;
        gbc_textField_2.gridy = 3;
        panel_6.add(textField_2, gbc_textField_2);
        textField_2.setColumns(10);
        textField_2.setText("" + surface.getJointSize());
        textField_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                surface.setJointSize(Integer.parseInt(textField_2.getText()));
                surface.onMoved();
                controller.notifyObserverForSurfaces();
            }
        });
        JPanel panel_3 = new JPanel();
        panel_3.setLayout(new GridLayout(0, 1, 0, 0));
        panel_3.setBackground(Color.black);
        add(panel_3);
    }
}