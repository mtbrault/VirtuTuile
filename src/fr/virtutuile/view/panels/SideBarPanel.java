package fr.virtutuile.view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import fr.virtutuile.domain.Material;
import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.SurfacesControllerObserver;
import fr.virtutuile.view.frames.MainWindow;

public class SideBarPanel extends JPanel implements SurfacesControllerObserver {
	private MainWindow mainWindow;
	private JPanel surfacesPanel = new JPanel();
	private JPanel materialsPanel = new JPanel();

	public SideBarPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		buildUp();
		mainWindow.controller.registerObserver(this);

	}

	private void buildUp() {
		setLayout(new GridLayout(3, 1, 0, 0));
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(350, 900));
		setBorder(BorderFactory.createLineBorder(Color.black));

		JPanel panel = new SelectedTilePanel(mainWindow.controller);
		JLabel lblNewLabel_1 = new JLabel("Découpage de la tuile");
		lblNewLabel_1.setVerticalAlignment(SwingConstants.BOTTOM);
		panel.add(lblNewLabel_1);
		;
		add(panel);

		materialsPanel.setLayout(new GridLayout(0, 1, 0, 0));
		JScrollPane scrollPaneMaterialPanel = new JScrollPane(materialsPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPaneMaterialPanel, materialsPanel);

		surfacesPanel.setLayout(new GridLayout(0, 1, 0, 0));
		JScrollPane scrollPane = new JScrollPane(surfacesPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, surfacesPanel);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void notifyObserver() {
		int nbSurface = 0;
		surfacesPanel.removeAll();
		surfacesPanel.updateUI();
		for (Surface surface : mainWindow.controller.getSurfaces()) {
			nbSurface++;
			surfacesPanel.add(new SideBarPanelSurface(surface, nbSurface, mainWindow.controller));
		}
		materialsPanel.removeAll();
		materialsPanel.updateUI();
		for (Material material : mainWindow.controller.getMaterials()) {
			materialsPanel.add(new MaterialPanel(material, mainWindow.controller));
		}
		materialsPanel.repaint();
		surfacesPanel.repaint();

		repaint();
	}


}