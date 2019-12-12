package fr.virtutuile.view.panels;

import fr.virtutuile.domain.Material;
import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.ZoomControllerObserver;
import fr.virtutuile.drawer.GridDrawer;
import fr.virtutuile.view.frames.MainWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

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

		JLabel labelGrid = new JLabel("Taille de la grille :");
		add(labelGrid, "cell 0 1");

		textFieldGrid = new JTextField();
		textFieldGrid.setColumns(10);
		textFieldGrid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("");
			}
		});
		add(textFieldGrid, "cell 1 1,growx");

		labelSurfaceNumber = new JLabel("Taille  de detection");
		add(labelSurfaceNumber, "cell 4 1");

		textFieldDetect = new JTextField();
		textFieldDetect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.controller.detectTile(Integer.parseInt(textFieldDetect.getText()));
				
			}
		});
		add(textFieldDetect, "cell 6 1,growx");
		textFieldDetect.setColumns(10);
	}

	private void buildUp() {
		Dimension footerDimension = this.getPreferredSize();
		footerDimension.height = 45;
		setPreferredSize(new Dimension(689, 59));
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new MigLayout("", "[8px][][][][][][grow][124.00,grow][]", "[8px][]"));
		labelZoom = new JLabel("Zoom : " + (int) (mainWindow.controller.getZoom() * 100) + "%");
		add(labelZoom, "cell 0 0,grow");
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
