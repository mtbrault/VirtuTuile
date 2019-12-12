package fr.virtutuile.view.panels;

import fr.virtutuile.domain.Material;
import fr.virtutuile.domain.VirtuTuileController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MaterialPanel extends JPanel {
	private JTextField textField_1;
	private JTextField textField;
	private JTextField textFieldColor;

	MaterialPanel(Material material, VirtuTuileController controller) {
		JPanel panel_6 = new JPanel();
		add(panel_6);
		panel_6.setBackground(Color.WHITE);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[] { 139, 206, 0 };
		gbl_panel_6.rowHeights = new int[] { 18, 0, 0, 0, 0, 0 };
		gbl_panel_6.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_6.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_6.setLayout(gbl_panel_6);
		JLabel lblSurfaceX = new JLabel("Matériaux : " + material.getName());
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

		textField = new JTextField("" + material.getWidth());
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
		panel_6.add(textField, gbc_textField);

		JLabel lblNewLabel = new JLabel("Hauteur");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		panel_6.add(lblNewLabel, gbc_lblNewLabel);

		textField_1 = new JTextField("" + material.getHeight());
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
		panel_6.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);

		JLabel labelColor = new JLabel("Color");
		labelColor.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints colorGrid = new GridBagConstraints();
		colorGrid.insets = new Insets(0, 0, 5, 5);
		colorGrid.anchor = GridBagConstraints.WEST;
		colorGrid.gridx = 0;
		colorGrid.gridy = 3;
		panel_6.add(labelColor, colorGrid);

		textFieldColor = new JTextField();
		textFieldColor.setColumns(10);
		textFieldColor.setText(material.getColor());
		textFieldColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				material.setColor(textFieldColor.getText());
				controller.notifyObserverForSurfaces();
			}
		});

		GridBagConstraints gridColor = new GridBagConstraints();
		gridColor.insets = new Insets(0, 0, 5, 0);
		gridColor.fill = GridBagConstraints.HORIZONTAL;
		gridColor.gridx = 1;
		gridColor.gridy = 3;
		panel_6.add(textFieldColor, gridColor);
		textFieldColor.setColumns(10);
	}
}
