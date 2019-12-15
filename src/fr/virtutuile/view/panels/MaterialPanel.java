package fr.virtutuile.view.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.virtutuile.domain.Material;
import fr.virtutuile.domain.VirtuTuileController;
import fr.virtutuile.view.panels.ColorChangePanel.ColorChangedListener;

public class MaterialPanel extends JPanel implements ColorChangedListener {
	private JPanel materialPanel;
	private ColorChangePanel colorChange;
	private Material material;
	private VirtuTuileController controller;

	public MaterialPanel(Material material, VirtuTuileController controller) {
		this.materialPanel = new JPanel();		
		this.material = material;
		this.controller = controller;
		this.buildUp();
	}

	public void buildUp() {
		add(materialPanel);
		materialPanel.setBackground(Color.WHITE);
		GridBagLayout gbl_materialPanel = new GridBagLayout();
		gbl_materialPanel.columnWidths = new int[] { 139, 206, 0 };
		gbl_materialPanel.rowHeights = new int[] { 18, 0, 0, 0, 0, 0 };
		gbl_materialPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_materialPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		materialPanel.setLayout(gbl_materialPanel);
		JLabel lblSurfaceX = new JLabel("Nom : " + material.getName());
		lblSurfaceX.setFont(new Font("Dialog", Font.BOLD, 15));
		GridBagConstraints gbc_lblSurfaceX = new GridBagConstraints();
		gbc_lblSurfaceX.insets = new Insets(0, 0, 5, 5);
		gbc_lblSurfaceX.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSurfaceX.gridx = 0;
		gbc_lblSurfaceX.gridy = 0;
		materialPanel.add(lblSurfaceX, gbc_lblSurfaceX);

		JLabel lblHauteur = new JLabel("Largeur :");
		lblHauteur.setHorizontalAlignment(SwingConstants.LEFT);
		lblHauteur.setVerticalAlignment(SwingConstants.BOTTOM);
		GridBagConstraints gbc_lblHauteur = new GridBagConstraints();
		gbc_lblHauteur.anchor = GridBagConstraints.WEST;
		gbc_lblHauteur.insets = new Insets(0, 0, 5, 5);
		gbc_lblHauteur.gridx = 0;
		gbc_lblHauteur.gridy = 1;
		materialPanel.add(lblHauteur, gbc_lblHauteur);

		JTextField textField = new JTextField("" + material.getWidth());
		textField.setColumns(10);
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				material.setWidth(Integer.parseInt(textField.getText()));
				controller.rebuildAllSurface();
				controller.notifyObserverForSurfaces();
			}
		});

		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		materialPanel.add(textField, gbc_textField);

		JLabel lblNewLabel = new JLabel("Hauteur :");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		materialPanel.add(lblNewLabel, gbc_lblNewLabel);

		JTextField textField_1 = new JTextField("" + material.getHeight());
		textField_1.setColumns(10);
		textField_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				material.setHeight(Integer.parseInt(textField_1.getText()));
				controller.rebuildAllSurface();
				controller.notifyObserverForSurfaces();
			}
		});
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		materialPanel.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);

		JLabel labelColor = new JLabel("Couleur :");
		labelColor.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints colorGrid = new GridBagConstraints();
		colorGrid.insets = new Insets(0, 0, 5, 5);
		colorGrid.anchor = GridBagConstraints.WEST;
		colorGrid.gridx = 0;
		colorGrid.gridy = 3;
		materialPanel.add(labelColor, colorGrid);

		colorChange = new ColorChangePanel(Color.WHITE);
		colorChange.addColorChangedListener(this);

		GridBagConstraints gridColor = new GridBagConstraints();
		gridColor.insets = new Insets(0, 0, 5, 0);
		gridColor.fill = GridBagConstraints.HORIZONTAL;
		gridColor.gridx = 1;
		gridColor.gridy = 3;
		materialPanel.add(colorChange, gridColor);
		
		JLabel lblBoite = new JLabel("Tuile par boites :");
		GridBagConstraints gbc_lblBoite = new GridBagConstraints();
		gbc_lblBoite.insets = new Insets(0, 0, 5, 5);
		gbc_lblBoite.anchor = GridBagConstraints.WEST;
		gbc_lblBoite.gridx = 0;
		gbc_lblBoite.gridy = 4;
		materialPanel.add(lblBoite, gbc_lblBoite);

		JTextField textFieldBoite = new JTextField("" + material.getNbTileByBox());
		textFieldBoite.setColumns(10);
		textFieldBoite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				material.setNbTileByBox(Integer.parseInt(textFieldBoite.getText()));
				controller.rebuildAllSurface();
				controller.notifyObserverForSurfaces();
			}
		});
		textFieldBoite.setColumns(10);

		GridBagConstraints gbctextFieldBoite = new GridBagConstraints();
		gbctextFieldBoite.insets = new Insets(0, 0, 5, 0);
		gbctextFieldBoite.fill = GridBagConstraints.HORIZONTAL;
		gbctextFieldBoite.gridx = 1;
		gbctextFieldBoite.gridy = 4;
		materialPanel.add(textFieldBoite, gbctextFieldBoite);

	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void colorChanged(Color newColor) {
		colorChange.setSelectedColor(newColor, false);
		material.setColor(newColor);
		controller.notifyObserverForSurfaces();
	}

}
