package fr.virtutuile.view.menu;

import fr.virtutuile.domain.Material;
import fr.virtutuile.domain.VirtuTuileController;
import fr.virtutuile.view.frames.MainWindow;
import fr.virtutuile.view.panels.ColorChangePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TopMenu extends JMenuBar {
	private VirtuTuileController controller;

	public TopMenu(MainWindow mainWindow) {
		this.controller = mainWindow.controller;
		this.buildUp();
	}

	private void buildUp() {
		JMenu file = new JMenu("Options de fichier");
		JMenuItem fileSave = new JMenuItem("Sauvegarder");
		JMenuItem fileChoose = new JMenuItem("Charger");

		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setDialogTitle("Choose a directory to save your file: ");

		fileSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = jfc.showSaveDialog(null);
				if (value == JFileChooser.APPROVE_OPTION) {
					controller.saveObject(jfc.getSelectedFile());
				}
			}
		});
		JFileChooser jfc1 = new JFileChooser(System.getProperty("user.dir"));
		jfc1.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".ser file", "ser");
		jfc1.addChoosableFileFilter(filter);
		jfc1.setDialogTitle("Choose a file to load");
		fileChoose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = jfc1.showOpenDialog(null);
				if (value == JFileChooser.APPROVE_OPTION)
					controller.loadObject(jfc1.getSelectedFile());
			}
		});
		file.add(fileChoose);
		file.add(fileSave);
		add(file);
	}
}
