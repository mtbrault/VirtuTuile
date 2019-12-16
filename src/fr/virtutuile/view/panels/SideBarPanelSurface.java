package fr.virtutuile.view.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import fr.virtutuile.domain.Material;
import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.VirtuTuileController;
import fr.virtutuile.view.panels.ColorChangePanel.ColorChangedListener;

public class SideBarPanelSurface extends JPanel implements ColorChangedListener {
	private JTextField textEpaisseurJoin;
	private JTextField textLargeur;
	private JTextField textHauteur;
	private JComboBox comboAlign;
	private JComboBox comboMaterial;
	private JComboBox comboMotif;
	private JComboBox comboPaste;
	private JComboBox comboCenter;
	private ColorChangePanel colorChange;
	private Surface surface;
	private VirtuTuileController controller;
	private JPanel blockPanel;
	private int nbSurface;
	private boolean tuileDirection;
	private JButton btnTuileDirection;

	public SideBarPanelSurface(Surface surface, int nbSurface, VirtuTuileController controller) {
		this.surface = surface;
		this.nbSurface = nbSurface;
		this.controller = controller;
		this.blockPanel = new JPanel();
		this.tuileDirection = false;

		this.btnTuileDirection = new JButton("HORIZONTAL");
		this.textHauteur = new JTextField("" + surface.getHeight());
		this.textLargeur = new JTextField("" + surface.getWidth());
		this.textEpaisseurJoin = new JTextField();
		this.buildUp();
	}

	public void buildUp() {

		add(blockPanel);
		if (surface.isSelected()) {
			blockPanel.setBackground(UIManager.getColor("CheckBox.focus"));
		} else {
			blockPanel.setBackground(Color.WHITE);
		}

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

		JButton btnAfficher = new JButton(surface.getMasked() ? "Masquer" : "Afficher");
		btnAfficher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				surface.setMasked(!surface.getMasked());
				if (surface.getMasked()) {
					btnAfficher.setText("Afficher");
					btnAfficher.repaint();
					controller.notifyObserverForSurfaces();
				} else {
					btnAfficher.setText("Masquer");
					btnAfficher.repaint();
					controller.notifyObserverForSurfaces();
				}
			}
		});
		GridBagConstraints gbc_btnAfficbher = new GridBagConstraints();
		gbc_btnAfficbher.insets = new Insets(0, 0, 5, 0);
		gbc_btnAfficbher.gridx = 1;
		gbc_btnAfficbher.gridy = 0;
		blockPanel.add(btnAfficher, gbc_btnAfficbher);

		if (surface.getMasked() == true) {

			JLabel lblInformation = new JLabel("Information :");
			lblInformation.setHorizontalAlignment(SwingConstants.LEFT);
			lblInformation.setVerticalAlignment(SwingConstants.BOTTOM);
			GridBagConstraints gbc_lblInformation = new GridBagConstraints();
			gbc_lblInformation.insets = new Insets(0, 0, 5, 5);
			gbc_lblInformation.gridx = 0;
			gbc_lblInformation.gridy = 1;
			blockPanel.add(lblInformation, gbc_lblInformation);

			JLabel lblBoiteNbr = new JLabel("Nombre de boites : " + surface.getNbBoxNeedForMaterial());
			lblBoiteNbr.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gbc_lblBoiteNbr = new GridBagConstraints();
			gbc_lblBoiteNbr.insets = new Insets(0, 0, 5, 0);
			gbc_lblBoiteNbr.gridx = 1;
			gbc_lblBoiteNbr.gridy = 1;
			blockPanel.add(lblBoiteNbr, gbc_lblBoiteNbr);

			JLabel lblLargeur = new JLabel("Largeur :");
			lblLargeur.setHorizontalAlignment(SwingConstants.LEFT);
			lblLargeur.setVerticalAlignment(SwingConstants.BOTTOM);
			GridBagConstraints gbc_Largeur = new GridBagConstraints();
			gbc_Largeur.anchor = GridBagConstraints.WEST;
			gbc_Largeur.insets = new Insets(0, 0, 5, 5);
			gbc_Largeur.gridx = 0;
			gbc_Largeur.gridy = 2;
			blockPanel.add(lblLargeur, gbc_Largeur);

			textLargeur.setColumns(10);
			textLargeur.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					surface.setWidth(Integer.parseInt(textLargeur.getText()));
					surface.onMoved();
					controller.notifyObserverForSurfaces();
				}
			});

			GridBagConstraints gbc_textLargeur = new GridBagConstraints();
			gbc_textLargeur.insets = new Insets(0, 0, 5, 0);
			gbc_textLargeur.fill = GridBagConstraints.HORIZONTAL;
			gbc_textLargeur.gridx = 1;
			gbc_textLargeur.gridy = 2;
			blockPanel.add(textLargeur, gbc_textLargeur);

			JLabel lblNewLabel = new JLabel("Hauteur :");
			lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 3;
			blockPanel.add(lblNewLabel, gbc_lblNewLabel);

			textHauteur.setColumns(10);
			textHauteur.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					surface.setHeight(Integer.parseInt(textHauteur.getText()));
					surface.onMoved();
					controller.notifyObserverForSurfaces();
				}
			});
			GridBagConstraints gbc_textHauteur = new GridBagConstraints();
			gbc_textHauteur.insets = new Insets(0, 0, 5, 0);
			gbc_textHauteur.fill = GridBagConstraints.HORIZONTAL;
			gbc_textHauteur.gridx = 1;
			gbc_textHauteur.gridy = 3;
			blockPanel.add(textHauteur, gbc_textHauteur);
			textHauteur.setColumns(10);

			JLabel lblEpaisseurDuJoin = new JLabel("Epaisseur du joint :");
			lblEpaisseurDuJoin.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gbc_lblEpaisseurDuJoin = new GridBagConstraints();
			gbc_lblEpaisseurDuJoin.anchor = GridBagConstraints.WEST;
			gbc_lblEpaisseurDuJoin.insets = new Insets(0, 0, 5, 5);
			gbc_lblEpaisseurDuJoin.gridx = 0;
			gbc_lblEpaisseurDuJoin.gridy = 4;
			blockPanel.add(lblEpaisseurDuJoin, gbc_lblEpaisseurDuJoin);

			GridBagConstraints gbc_EpaisseurJoin = new GridBagConstraints();
			gbc_EpaisseurJoin.insets = new Insets(0, 0, 5, 0);
			gbc_EpaisseurJoin.fill = GridBagConstraints.HORIZONTAL;
			gbc_EpaisseurJoin.gridx = 1;
			gbc_EpaisseurJoin.gridy = 4;
			blockPanel.add(textEpaisseurJoin, gbc_EpaisseurJoin);
			textEpaisseurJoin.setColumns(10);
			textEpaisseurJoin.setText("" + surface.getJointSize());
			textEpaisseurJoin.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (textEpaisseurJoin.getText() != "") {
						surface.setJointSize(Integer.parseInt(textEpaisseurJoin.getText()));
						surface.onMoved();
						controller.notifyObserverForSurfaces();
					}
				}
			});

			JLabel labelMaterial = new JLabel("Matériaux :");
			GridBagConstraints gridMaterial = new GridBagConstraints();
			gridMaterial.anchor = GridBagConstraints.WEST;
			gridMaterial.insets = new Insets(0, 0, 5, 5);
			gridMaterial.gridx = 0;
			gridMaterial.gridy = 5;
			blockPanel.add(labelMaterial, gridMaterial);

			comboMaterial = new JComboBox(controller.getMaterials().toArray());
			comboMaterial
					.setSelectedIndex(controller.getSurfaceMaterialIndex(surface, controller.getMaterials().toArray()));

			comboMaterial.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JComboBox comboBox = (JComboBox) arg0.getSource();
					Material material = (Material) comboMaterial.getSelectedItem();
					controller.setSurfaceMaterial(surface, material);
					controller.rebuildAllSurface();
					controller.notifyObserverForSurfaces();
				}
			});

			GridBagConstraints gridBtnMaterial = new GridBagConstraints();
			gridBtnMaterial.insets = new Insets(0, 0, 5, 0);
			gridBtnMaterial.gridx = 1;
			gridBtnMaterial.gridy = 5;
			blockPanel.add(comboMaterial, gridBtnMaterial);

			JLabel labelMotif = new JLabel("Motifs :");
			GridBagConstraints gridMotif = new GridBagConstraints();
			gridMotif.anchor = GridBagConstraints.WEST;
			gridMotif.insets = new Insets(0, 0, 5, 5);
			gridMotif.gridx = 0;
			gridMotif.gridy = 6;
			blockPanel.add(labelMotif, gridMotif);

			String[] paternList = new String[] { "1", "2", "3", "4" };

			comboMotif = new JComboBox(paternList);
			comboMotif.setSelectedIndex(surface.getPatternId());

			comboMotif.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JComboBox comboBox = (JComboBox) arg0.getSource();
					String paternNbr = (String) comboMotif.getSelectedItem();
					surface.changePattern(Integer.parseInt(paternNbr) - 1);
					controller.rebuildAllSurface();
					controller.notifyObserverForSurfaces();
				}
			});
			GridBagConstraints gridBtnMotif = new GridBagConstraints();
			gridBtnMotif.insets = new Insets(0, 0, 5, 0);
			gridBtnMotif.gridx = 1;
			gridBtnMotif.gridy = 6;
			blockPanel.add(comboMotif, gridBtnMotif);

			colorChange = new ColorChangePanel(surface.getColor());
			colorChange.addColorChangedListener(this);

			JLabel labelColor = new JLabel("Couleur : ");
			GridBagConstraints gridColor = new GridBagConstraints();
			gridColor.anchor = GridBagConstraints.WEST;
			gridColor.insets = new Insets(0, 0, 5, 5);
			gridColor.gridx = 0;
			gridColor.gridy = 7;
			blockPanel.add(labelColor, gridColor);

			GridBagConstraints gridTextColor = new GridBagConstraints();
			gridTextColor.insets = new Insets(0, 0, 5, 0);
			gridTextColor.fill = GridBagConstraints.HORIZONTAL;

			gridTextColor.gridx = 1;
			gridTextColor.gridy = 7;
			blockPanel.add(colorChange, gridTextColor);

			JLabel labelSurfaceAction = new JLabel("Modifier selon surface :");
			GridBagConstraints gridSurface = new GridBagConstraints();
			gridSurface.anchor = GridBagConstraints.WEST;
			gridSurface.insets = new Insets(0, 0, 5, 5);
			gridSurface.gridx = 0;
			gridSurface.gridy = 8;
			blockPanel.add(labelSurfaceAction, gridSurface);

			JButton btnSurface = new JButton("Changer");
			btnSurface.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JTextField xField = new JTextField(5);
					JTextField yField = new JTextField(5);

					JPanel myPopup = new JPanel();
					myPopup.add(new JLabel("x:"));
					myPopup.add(xField);
					myPopup.add(Box.createHorizontalStrut(15)); // a spacer
					myPopup.add(new JLabel("y:"));
					myPopup.add(yField);

					int result = JOptionPane.showConfirmDialog(null, myPopup, "Entrer les valeurs de x et y",
							JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						controller.changeSurfaceWithSurfaceCoord(surface, Integer.parseInt(xField.getText()),
								Integer.parseInt(yField.getText()));
						controller.rebuildAllSurface();
						controller.notifyObserverForSurfaces();
					}
				}
			});
			GridBagConstraints gridBtnSurface = new GridBagConstraints();
			gridBtnSurface.insets = new Insets(0, 0, 5, 0);
			gridBtnSurface.gridx = 1;
			gridBtnSurface.gridy = 8;
			blockPanel.add(btnSurface, gridBtnSurface);

			JLabel labelAlign = new JLabel("Aligner :");
			GridBagConstraints gridAlign = new GridBagConstraints();
			gridAlign.anchor = GridBagConstraints.WEST;
			gridAlign.insets = new Insets(0, 0, 5, 5);
			gridAlign.gridx = 0;
			gridAlign.gridy = 9;
			blockPanel.add(labelAlign, gridAlign);

			Vector<DirectionType> typeArray = new Vector<DirectionType>();
			typeArray.addElement(new DirectionType("Modifier", -1, 0));
			typeArray.addElement(new DirectionType("Horizontalement", 0, 0));
			typeArray.addElement(new DirectionType("Verticalement", 1, 0));

			Vector<DirectionType> directionArray = new Vector<DirectionType>();
			directionArray.addElement(new DirectionType("Modifier", 0, 0));
			directionArray.addElement(new DirectionType("gauche", -1, 0));
			directionArray.addElement(new DirectionType("droite", 1, 0));
			directionArray.addElement(new DirectionType("haut", 0, -1));
			directionArray.addElement(new DirectionType("bas", 0, 1));

			comboAlign = new JComboBox(directionArray);

			comboAlign.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JComboBox comboBox = (JComboBox) arg0.getSource();
					DirectionType alignType = (DirectionType) comboBox.getSelectedItem();
					if (!(alignType.getX() == 0 && alignType.getY() == 0)) {
						controller.alignSurface(surface, alignType.getX(), alignType.getY());
					} else {
						JOptionPane.showMessageDialog(null, "Veuillez selectionnez une valeur pour aligner");

					}
					controller.rebuildAllSurface();
					controller.notifyObserverForSurfaces();
				}
			});

			GridBagConstraints gridAlignCombo = new GridBagConstraints();
			gridAlignCombo.insets = new Insets(0, 0, 5, 0);
			gridAlignCombo.gridx = 1;
			gridAlignCombo.gridy = 9;
			blockPanel.add(comboAlign, gridAlignCombo);

			JLabel labelPaste = new JLabel("Coller :");
			GridBagConstraints gridPaste = new GridBagConstraints();
			gridPaste.anchor = GridBagConstraints.WEST;
			gridPaste.insets = new Insets(0, 0, 5, 5);
			gridPaste.gridx = 0;
			gridPaste.gridy = 10;
			blockPanel.add(labelPaste, gridPaste);

			comboPaste = new JComboBox(typeArray);
			comboPaste.setEnabled(true);
			comboPaste.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JComboBox comboBox = (JComboBox) arg0.getSource();
					DirectionType pasteType = (DirectionType) comboBox.getSelectedItem();
					if (pasteType.getX() != -1) {
						controller.pastSurface(surface, pasteType.getX());
					} else {
						JOptionPane.showMessageDialog(null, "Veuillez selectionnez une valeur pour coller");
					}
					controller.rebuildAllSurface();
					controller.notifyObserverForSurfaces();
				}
			});

			GridBagConstraints gridPasteCombo = new GridBagConstraints();
			gridPasteCombo.insets = new Insets(0, 0, 5, 0);
			gridPasteCombo.gridx = 1;
			gridPasteCombo.gridy = 10;
			blockPanel.add(comboPaste, gridPasteCombo);

			JLabel labelCenter = new JLabel("Centre :");
			GridBagConstraints gridCenter = new GridBagConstraints();
			gridCenter.insets = new Insets(0, 0, 0, 5);
			gridCenter.anchor = GridBagConstraints.WEST;
			gridPaste.insets = new Insets(0, 0, 0, 5);
			gridCenter.gridx = 0;
			gridCenter.gridy = 11;
			blockPanel.add(labelCenter, gridCenter);

			comboCenter = new JComboBox(typeArray);
			comboCenter.setEnabled(true);
			comboCenter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComboBox comboBox = (JComboBox) e.getSource();
					DirectionType centerType = (DirectionType) comboBox.getSelectedItem();
					if (centerType.getX() != -1) {
						controller.centerSurface(surface, centerType.getX());
					} else {
						JOptionPane.showMessageDialog(null, "Veuillez sélectionner une valeur pour centrer");
					}
				}
			});

			GridBagConstraints gridCenterCombo = new GridBagConstraints();
			gridCenterCombo.gridx = 1;
			gridCenterCombo.gridy = 11;
			blockPanel.add(comboCenter, gridCenterCombo);

			JLabel labelTileShift = new JLabel("Décalage :");
			labelTileShift.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gbc_tileShift = new GridBagConstraints();
			gbc_tileShift.insets = new Insets(0, 0, 5, 5);
			gbc_tileShift.anchor = GridBagConstraints.WEST;
			gbc_tileShift.gridx = 0;
			gbc_tileShift.gridy = 12;
			blockPanel.add(labelTileShift, gbc_tileShift);

			JTextField textfieldTileShift = new JTextField("" + surface.getTileShift());
			textfieldTileShift.setColumns(10);
			textfieldTileShift.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Double val = Double.parseDouble(textfieldTileShift.getText());
					if (val == 1.0) {
						val = 0.9;
					} else if (val < 0) {
						val = 0.1;
					}
					surface.setTileShift(val);
					controller.notifyObserverForSurfaces();
					controller.rebuildAllSurface();
				}
			});
			GridBagConstraints gbc_textFieldTile = new GridBagConstraints();
			gbc_textFieldTile.insets = new Insets(0, 0, 5, 0);
			gbc_textFieldTile.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldTile.gridx = 1;
			gbc_textFieldTile.gridy = 12;
			blockPanel.add(textfieldTileShift, gbc_textFieldTile);
			textfieldTileShift.setColumns(10);

			JLabel labelTuileDirection = new JLabel("Direction des tuiles :");
			GridBagConstraints gridTuileDirection = new GridBagConstraints();
			gridTuileDirection.anchor = GridBagConstraints.WEST;
			gridTuileDirection.insets = new Insets(0, 0, 5, 5);
			gridTuileDirection.gridx = 0;
			gridTuileDirection.gridy = 13;
			blockPanel.add(labelTuileDirection, gridTuileDirection);

			btnTuileDirection.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					tuileDirection = !tuileDirection;
					if (tuileDirection == true) {
						btnTuileDirection.setText("VERTICAL");
						btnTuileDirection.repaint();
						surface.setVertical();
						controller.notifyObserverForSurfaces();
						controller.rebuildAllSurface();
					} else if (tuileDirection == false) {
						btnTuileDirection.setText("HORIZONTAL");
						btnTuileDirection.repaint();
						surface.setVertical();
						controller.notifyObserverForSurfaces();
						controller.rebuildAllSurface();
					}

				}
			});
			GridBagConstraints gridBtnTuile = new GridBagConstraints();
			gridBtnTuile.insets = new Insets(0, 0, 5, 0);
			gridBtnTuile.gridx = 1;
			gridBtnTuile.gridy = 13;
			blockPanel.add(btnTuileDirection, gridBtnTuile);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	class DirectionType {
		private int x;
		private int y;
		private String direction;

		public DirectionType(String direction, int x, int y) {
			this.direction = direction;
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}

		public String getDirection() {
			return this.direction;
		}

		@Override
		public String toString() {
			return this.direction.toUpperCase();
		}

	}

	@Override
	public void colorChanged(Color newColor) {
		colorChange.setSelectedColor(newColor, false);
		surface.setColor(newColor);
		controller.notifyObserverForSurfaces();
		colorChange.repaint();
	}
}