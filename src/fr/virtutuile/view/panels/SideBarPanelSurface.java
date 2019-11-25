package fr.virtutuile.view.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.VirtuTuileController;

import javax.swing.SwingConstants;
import javax.swing.JSplitPane;
import javax.swing.JButton;

public class SideBarPanelSurface extends JPanel {
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField;

    public SideBarPanelSurface(Surface surface, int nbSurface, VirtuTuileController controller) {
        JPanel blockPanel = new JPanel();
        add(blockPanel);
        blockPanel.setBackground(Color.WHITE);
        GridBagLayout gbl_blockPanel = new GridBagLayout();
        gbl_blockPanel.columnWidths = new int[] { 139, 194, 0 };
        gbl_blockPanel.rowHeights = new int[] { 18, 0, 0, 0, 0, 0 };
        gbl_blockPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_blockPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        blockPanel.setLayout(gbl_blockPanel);
        JLabel lblSurfaceX = new JLabel("Surface " + nbSurface);
        lblSurfaceX.setFont(new Font("Dialog", Font.BOLD, 15));
        GridBagConstraints gbc_lblSurfaceX = new GridBagConstraints();
        gbc_lblSurfaceX.insets = new Insets(0, 0, 5, 5);
        gbc_lblSurfaceX.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblSurfaceX.gridx = 0;
        gbc_lblSurfaceX.gridy = 0;
        blockPanel.add(lblSurfaceX, gbc_lblSurfaceX);

        JLabel lblHauteur = new JLabel("Largeur");
        lblHauteur.setHorizontalAlignment(SwingConstants.LEFT);
        lblHauteur.setVerticalAlignment(SwingConstants.BOTTOM);
        GridBagConstraints gbc_lblHauteur = new GridBagConstraints();
        gbc_lblHauteur.anchor = GridBagConstraints.WEST;
        gbc_lblHauteur.insets = new Insets(0, 0, 5, 5);
        gbc_lblHauteur.gridx = 0;
        gbc_lblHauteur.gridy = 1;
        blockPanel.add(lblHauteur, gbc_lblHauteur);

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
        blockPanel.add(textField, gbc_textField);

        JLabel lblNewLabel = new JLabel("Hauteur");
        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 2;
        blockPanel.add(lblNewLabel, gbc_lblNewLabel);

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
        blockPanel.add(textField_1, gbc_textField_1);
        textField_1.setColumns(10);

        JLabel lblEpaisseurDuJoin = new JLabel("Epaisseur du joint");
        lblEpaisseurDuJoin.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gbc_lblEpaisseurDuJoin = new GridBagConstraints();
        gbc_lblEpaisseurDuJoin.anchor = GridBagConstraints.WEST;
        gbc_lblEpaisseurDuJoin.insets = new Insets(0, 0, 5, 5);
        gbc_lblEpaisseurDuJoin.gridx = 0;
        gbc_lblEpaisseurDuJoin.gridy = 3;
        blockPanel.add(lblEpaisseurDuJoin, gbc_lblEpaisseurDuJoin);

        textField_2 = new JTextField();
        GridBagConstraints gbc_textField_2 = new GridBagConstraints();
        gbc_textField_2.insets = new Insets(0, 0, 5, 0);
        gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_2.gridx = 1;
        gbc_textField_2.gridy = 3;
        blockPanel.add(textField_2, gbc_textField_2);
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
        
        JLabel labelMaterial = new JLabel("Matériaux");
        GridBagConstraints gridMaterial = new GridBagConstraints();
        gridMaterial.anchor = GridBagConstraints.WEST;
        gridMaterial.insets = new Insets(0, 0, 0, 5);
        gridMaterial.gridx = 0;
        gridMaterial.gridy = 4;
        blockPanel.add(labelMaterial, gridMaterial);
        
        JButton btnMaterial = new JButton("Changer");
        btnMaterial.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        	    controller.changeSurfaceMaterial(surface);
        	    controller.rebuildAllSurface();
                controller.notifyObserverForSurfaces();
        	}
        });
        GridBagConstraints gridBtnMaterial = new GridBagConstraints();
        gridBtnMaterial.gridx = 1;
        gridBtnMaterial.gridy = 4;
        blockPanel.add(btnMaterial, gridBtnMaterial);

        
        
        JLabel labelMotif = new JLabel("Motifs");
        GridBagConstraints gridMotif = new GridBagConstraints();
        gridMotif.anchor = GridBagConstraints.WEST;
        gridMotif.insets = new Insets(0, 0, 0, 5);
        gridMotif.gridx = 0;
        gridMotif.gridy = 5;
        blockPanel.add(labelMotif, gridMotif);
        
        JButton btnMotif = new JButton("Changer");
        btnMotif.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	    surface.changePattern();
                controller.rebuildAllSurface();
                controller.notifyObserverForSurfaces();
        	}
        });
        GridBagConstraints gridBtnMotif = new GridBagConstraints();
        gridBtnMotif.gridx = 1;
        gridBtnMotif.gridy = 5;
        blockPanel.add(btnMotif, gridBtnMotif);
   
    }
}