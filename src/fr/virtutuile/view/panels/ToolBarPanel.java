package fr.virtutuile.view.panels;

import fr.virtutuile.domain.Material;
import fr.virtutuile.domain.State;
import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.Point;
import fr.virtutuile.view.Main;
import fr.virtutuile.view.frames.MainWindow;
import fr.virtutuile.view.listeners.action.ToolBarAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ToolBarPanel extends JPanel {

	private MainWindow mainWindow;
	private JButton buttonSelect;
	private JButton buttonMove;
	private JButton buttonMooveZone;
	private JButton buttonSurfaceRect;
	private JButton buttonIrregular;
	private JButton buttonCutSurface;
	private JButton buttonMoveHole;
	private JButton buttonMoveTile;
	private JButton buttonMovePattern;

	String undo = "<html>Undo</html>";
	String redo = "<html>Redo</html>";
	String save = "<html><center>Save</center></html>";
	String load = "<html><center>Load</center></html>";
	String delete = "<html><center>Supprimer</center></html>";
	String select = "<html><center>Selection</center></html>";
	String mooveZone = "<html><center>Déplacer </br> Zone</center><</html>";
	String mooveSurface = "<html><center>Déplacer </br> Surface</center></html>";
	String displayGrid = "<html><center>Afficher </br> Grille</center></html>";
	String moove = "<html>undo</html>";
	String addSurfaceRect = "<html><center>Surface </br> Rectangle</center></html>";
	String addSurfaceIreg = "<html><center>Surface </br> Irreguliere</center></html>";
	String fusion = "<html><center>Fusionné</center></html>";
	String addMaterial = "<html><center>Ajouter </br> Matériaux</center></html>";
	String createHole = "<html><center>Créer </br> Trou</center></html>";
	String moovePatern = "<html><center>Déplacer </br> Pattern</center></html>";
	String addHole = "<html><center>Créer </br> Trou</center></html>";
	String moveHole = "<html><center>Déplacer </br> Trou</center></html>";
	String moveTile = "<html><center>Déplacer </br> Tuile</center></html>";

	public ToolBarPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		buttonMooveZone = new JButton(mooveZone);
		buttonSelect = new JButton(select);
		buttonMove = new JButton(mooveSurface);
		buttonSurfaceRect = new JButton(addSurfaceRect);
		buttonIrregular = new JButton(addSurfaceIreg);
		buttonMovePattern = new JButton(moovePatern);
		buttonCutSurface = new JButton(createHole);
		buttonMoveHole = new JButton(moveHole);
		buttonMoveTile = new JButton(moveTile);
		buildUp();
	}

	public BufferedImage resizeImage(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	private void handleClick(State state) {
		mainWindow.controller.setState(state);
	}

	private void buildUp() {

		setLayout(new GridLayout(1, 2, 0, 0));
		Dimension toolbarDimension = getPreferredSize();
		toolbarDimension.height = 60;
		setPreferredSize(new Dimension(920, 66));
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createLineBorder(Color.black));

		JPanel toolbarLeft = new JPanel();
		toolbarLeft.setLayout(new GridLayout(1, 11));
		add(toolbarLeft);

		JButton buttonUndo = new JButton(undo);
		buttonUndo.setForeground(Color.BLACK);
		buttonUndo.setBackground(Color.WHITE);
		buttonUndo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.controller.undo();
			}
		});
		toolbarLeft.add(buttonUndo);

		JButton buttonRedo = new JButton(redo);
		buttonRedo.setBackground(Color.WHITE);
		buttonRedo.setForeground(Color.BLACK);
		buttonRedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.controller.redo();
			}
		});
		toolbarLeft.add(buttonRedo);

		JButton buttonDelete = new JButton(delete);
		buttonDelete.setBackground(Color.WHITE);
		buttonDelete.setForeground(Color.BLACK);
		buttonDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainWindow.controller.getState() == State.SELECTION) {
					mainWindow.controller.deleteSurface();
				} else {
					JOptionPane.showMessageDialog(null, "Sélectionnez une surface à supprimer !");
				}
			}
		});
		toolbarLeft.add(buttonDelete);

		buttonMooveZone.setForeground(Color.BLACK);
		buttonMooveZone.setBackground(Color.WHITE);
		buttonMooveZone.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handleClick(State.MOVE);
				if (mainWindow.controller.getState() == State.MOVE) {
					buttonMooveZone.setBackground(Color.LIGHT_GRAY);
					buttonSelect.setBackground(Color.white);
					buttonMove.setBackground(Color.white);
					buttonSurfaceRect.setBackground(Color.white);
					buttonIrregular.setBackground(Color.white);
					buttonCutSurface.setBackground(Color.white);
					buttonMovePattern.setBackground(Color.white);
					buttonMoveHole.setBackground(Color.white);
				} else {
					buttonMooveZone.setBackground(Color.white);
				}
			}
		});

		toolbarLeft.add(buttonMooveZone);

		buttonSelect.setBackground(Color.WHITE);
		buttonSelect.setForeground(Color.BLACK);
		buttonSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handleClick(State.SELECTION);
				if (mainWindow.controller.getState() == State.SELECTION) {
					buttonSelect.setBackground(Color.LIGHT_GRAY);
					buttonMooveZone.setBackground(Color.white);
					buttonMove.setBackground(Color.white);
					buttonSurfaceRect.setBackground(Color.white);
					buttonIrregular.setBackground(Color.white);
					buttonCutSurface.setBackground(Color.white);
					buttonMovePattern.setBackground(Color.white);
					buttonMoveHole.setBackground(Color.white);
				} else {
					buttonSelect.setBackground(Color.white);
				}
			}
		});
		toolbarLeft.add(buttonSelect);

		buttonMove.setForeground(Color.BLACK);
		buttonMove.setBackground(Color.WHITE);
		buttonMove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handleClick(State.MOVE_SURFACE);
				if (mainWindow.controller.getState() == State.MOVE_SURFACE) {
					buttonMove.setBackground(Color.LIGHT_GRAY);
					buttonSelect.setBackground(Color.white);
					buttonMooveZone.setBackground(Color.white);
					buttonSurfaceRect.setBackground(Color.white);
					buttonIrregular.setBackground(Color.white);
					buttonCutSurface.setBackground(Color.white);
					buttonMovePattern.setBackground(Color.white);
					buttonMoveHole.setBackground(Color.white);
				} else {
					buttonMove.setBackground(Color.white);
				}
			}
		});
		toolbarLeft.add(buttonMove);

		JButton buttonGrid = new JButton(displayGrid);
		buttonGrid.setForeground(Color.BLACK);
		buttonGrid.setBackground(Color.WHITE);
		buttonGrid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.controller.switchGrid();
				if (mainWindow.controller.getGridSwitch()) {
					buttonGrid.setBackground(Color.LIGHT_GRAY);
				} else {
					buttonGrid.setBackground(Color.white);
				}
			}
		});
		toolbarLeft.add(buttonGrid);

		buttonSurfaceRect.setForeground(Color.BLACK);
		buttonSurfaceRect.setBackground(Color.WHITE);
		buttonSurfaceRect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handleClick(State.CREATE_RECTANGULAR_SURFACE);
				if (mainWindow.controller.getState() == State.CREATE_RECTANGULAR_SURFACE) {
					buttonSurfaceRect.setBackground(Color.LIGHT_GRAY);
					buttonMove.setBackground(Color.white);
					buttonSelect.setBackground(Color.white);
					buttonMooveZone.setBackground(Color.white);
					buttonIrregular.setBackground(Color.white);
					buttonCutSurface.setBackground(Color.white);
					buttonMovePattern.setBackground(Color.white);
					buttonMoveHole.setBackground(Color.white);
				} else {
					buttonSurfaceRect.setBackground(Color.white);
				}
			}
		});
		toolbarLeft.add(buttonSurfaceRect);

		buttonIrregular.setForeground(Color.BLACK);
		buttonIrregular.setBackground(Color.WHITE);
		buttonIrregular.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handleClick(State.CREATE_IRREGULAR_SURFACE);
				if (mainWindow.controller.getState() == State.CREATE_IRREGULAR_SURFACE) {
					buttonIrregular.setBackground(Color.LIGHT_GRAY);
					buttonSurfaceRect.setBackground(Color.white);
					buttonMove.setBackground(Color.white);
					buttonSelect.setBackground(Color.white);
					buttonMooveZone.setBackground(Color.white);
					buttonCutSurface.setBackground(Color.white);
					buttonMovePattern.setBackground(Color.white);
					buttonMoveHole.setBackground(Color.white);
				} else {
					buttonIrregular.setBackground(Color.white);
				}
			}
		});
		toolbarLeft.add(buttonIrregular);

		JButton buttonFusion = new JButton(fusion);
		buttonFusion.setForeground(Color.BLACK);
		buttonFusion.setBackground(Color.WHITE);
		buttonFusion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.controller.combineSelectedSurfaces();
			}
		});
		toolbarLeft.add(buttonFusion);

		JButton buttonAddMaterial = new JButton(addMaterial);
		buttonAddMaterial.setForeground(Color.BLACK);
		buttonAddMaterial.setBackground(Color.WHITE);
		buttonAddMaterial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField materialName = new JTextField(5);
				materialName.setColumns(10);

				JPanel myPopup = new JPanel();
				myPopup.add(new JLabel("Nom du matériau à créer :"));
				myPopup.add(materialName);

				int result = JOptionPane.showConfirmDialog(null, myPopup, "Ajouter un matériau",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					if (materialName.getText().length() < 1 || materialName.getText() == " ") {
						JOptionPane.showMessageDialog(null, "Le nom de votre matériau doit être plus grand !");
						return;
					}
					mainWindow.controller.addMaterial(new Material(materialName.getText()));
					mainWindow.controller.notifyObserverForSurfaces();
				}
			}
		});

		toolbarLeft.add(buttonAddMaterial);

		buttonMovePattern.setForeground(Color.BLACK);
		buttonMovePattern.setBackground(Color.WHITE);
		buttonMovePattern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handleClick(State.MOVE_PATTERN);
				if (mainWindow.controller.getState() == State.MOVE_PATTERN) {
					buttonMovePattern.setBackground(Color.LIGHT_GRAY);
					buttonIrregular.setBackground(Color.white);
					buttonSurfaceRect.setBackground(Color.white);
					buttonMove.setBackground(Color.white);
					buttonSelect.setBackground(Color.white);
					buttonMooveZone.setBackground(Color.white);
					buttonCutSurface.setBackground(Color.white);
					buttonMoveHole.setBackground(Color.white);
				} else {
					buttonMovePattern.setBackground(Color.white);
				}
			}
		});
		toolbarLeft.add(buttonMovePattern);

		buttonCutSurface.setBackground(Color.WHITE);
		buttonCutSurface.setForeground(Color.BLACK);
		buttonCutSurface.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handleClick(State.CUT_SURFACE);
				if (mainWindow.controller.getState() == State.CUT_SURFACE) {
					buttonCutSurface.setBackground(Color.LIGHT_GRAY);
					buttonMovePattern.setBackground(Color.white);
					buttonIrregular.setBackground(Color.white);
					buttonSurfaceRect.setBackground(Color.white);
					buttonMove.setBackground(Color.white);
					buttonSelect.setBackground(Color.white);
					buttonMooveZone.setBackground(Color.white);
					buttonMoveHole.setBackground(Color.white);
				} else {
					buttonCutSurface.setBackground(Color.white);
				}
			}
		});
		toolbarLeft.add(buttonCutSurface);

		buttonMoveHole.setBackground(Color.WHITE);
		buttonMoveHole.setForeground(Color.BLACK);
		buttonMoveHole.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handleClick(State.MOVE_HOLE);
				if (mainWindow.controller.getState() == State.MOVE_HOLE) {
					buttonMoveHole.setBackground(Color.LIGHT_GRAY);
					buttonMovePattern.setBackground(Color.white);
					buttonIrregular.setBackground(Color.white);
					buttonSurfaceRect.setBackground(Color.white);
					buttonMove.setBackground(Color.white);
					buttonSelect.setBackground(Color.white);
					buttonMooveZone.setBackground(Color.white);
					buttonCutSurface.setBackground(Color.white);
				} else {
					buttonMoveHole.setBackground(Color.white);
				}
			}
		});
		toolbarLeft.add(buttonMoveHole);
		
		buttonMoveTile.setBackground(Color.WHITE);
		buttonMoveTile.setForeground(Color.BLACK);
		buttonMoveTile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handleClick(State.MOVE_ONE_TILE);
				if (mainWindow.controller.getState() == State.MOVE_ONE_TILE) {
					buttonMoveTile.setBackground(Color.LIGHT_GRAY);
					buttonMoveHole.setBackground(Color.white);
					buttonMovePattern.setBackground(Color.white);
					buttonIrregular.setBackground(Color.white);
					buttonSurfaceRect.setBackground(Color.white);
					buttonMove.setBackground(Color.white);
					buttonSelect.setBackground(Color.white);
					buttonMooveZone.setBackground(Color.white);
					buttonCutSurface.setBackground(Color.white);
				} else {
					buttonMoveTile.setBackground(Color.white);
				}
			}
		});
		toolbarLeft.add(buttonMoveTile);
	}
}
