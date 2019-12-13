package fr.virtutuile.view.panels;

import fr.virtutuile.domain.ZoomControllerObserver;
import fr.virtutuile.drawer.GridDrawer;
import fr.virtutuile.view.frames.MainWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class FooterPanel extends JPanel implements ZoomControllerObserver {

	private final MainWindow mainWindow;
	private GridDrawer gridDrawer;
	private JLabel labelZoom;
	private JTextField textFieldGrid;
	private JLabel labelSurfaceNumber;
	private JTextField textFieldDetect;

	public FooterPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		this.gridDrawer = gridDrawer;
		buildUp();
		mainWindow.controller.registerObserverZoom(this);
	}

	private void buildUp() {
		Dimension footerDimension = this.getPreferredSize();
		footerDimension.height = 45;
		setPreferredSize(new Dimension(689, 59));
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.black));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 121, 110, 41, 130, 110, 0 };
		gridBagLayout.rowHeights = new int[] { 15, 19, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		textFieldDetect = new JTextField();
		textFieldDetect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.controller.detectTile(Integer.parseInt(textFieldDetect.getText()));

			}
		});
		labelZoom = new JLabel("Zoom : " + (int) (mainWindow.controller.getZoom() * 100) + "%");
		GridBagConstraints gbc_labelZoom = new GridBagConstraints();
		gbc_labelZoom.fill = GridBagConstraints.BOTH;
		gbc_labelZoom.insets = new Insets(0, 0, 5, 5);
		gbc_labelZoom.gridx = 0;
		gbc_labelZoom.gridy = 0;
		add(labelZoom, gbc_labelZoom);

		textFieldGrid = new JTextField("valeur par default a modifié");
		textFieldGrid.setColumns(10);
		textFieldGrid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(
						"here mettre le changement de size de la grid " + Integer.parseInt(textFieldGrid.getText()));
			}
		});

		JLabel labelGrid = new JLabel("Taille de la grille :");
		GridBagConstraints gbc_labelGrid = new GridBagConstraints();
		gbc_labelGrid.anchor = GridBagConstraints.WEST;
		gbc_labelGrid.insets = new Insets(0, 0, 0, 5);
		gbc_labelGrid.gridx = 0;
		gbc_labelGrid.gridy = 1;
		add(labelGrid, gbc_labelGrid);
		GridBagConstraints gbc_textFieldGrid = new GridBagConstraints();
		gbc_textFieldGrid.anchor = GridBagConstraints.NORTH;
		gbc_textFieldGrid.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldGrid.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldGrid.gridx = 1;
		gbc_textFieldGrid.gridy = 1;
		add(textFieldGrid, gbc_textFieldGrid);

		labelSurfaceNumber = new JLabel("Taille  de detection");
		GridBagConstraints gbc_labelSurfaceNumber = new GridBagConstraints();
		gbc_labelSurfaceNumber.anchor = GridBagConstraints.WEST;
		gbc_labelSurfaceNumber.insets = new Insets(0, 0, 0, 5);
		gbc_labelSurfaceNumber.gridx = 3;
		gbc_labelSurfaceNumber.gridy = 1;
		add(labelSurfaceNumber, gbc_labelSurfaceNumber);
		GridBagConstraints gbc_textFieldDetect = new GridBagConstraints();
		gbc_textFieldDetect.anchor = GridBagConstraints.NORTH;
		gbc_textFieldDetect.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDetect.gridx = 4;
		gbc_textFieldDetect.gridy = 1;
		add(textFieldDetect, gbc_textFieldDetect);
		textFieldDetect.setColumns(10);
	}

	@Override
	public void notifyUpdatedZoom() {
		int zoomValue = (int) (mainWindow.controller.getZoom() * 100);
		if (zoomValue > 1000) {
			labelZoom.setText("Zoom : > " + 1000 + "%");
			labelZoom.repaint();

		} else {
			labelZoom.setText("Zoom : " + zoomValue + "%");
			labelZoom.repaint();
		}
		repaint();
	}
}
