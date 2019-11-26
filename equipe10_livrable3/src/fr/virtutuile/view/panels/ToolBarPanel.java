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

public class ToolBarPanel extends JPanel {

    private MainWindow mainWindow;
    public ToolBarPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
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


    private ActionListener handleClick(State state) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(mainWindow.controller.getState());
                mainWindow.controller.setState(state);
                System.out.println(mainWindow.controller.getState());
            }
        };
    }
    private void buildUp() {

        setLayout(new GridLayout(1, 2, 0, 0));
        Dimension toolbarDimension = getPreferredSize();
        toolbarDimension.height = 60;
        setPreferredSize(toolbarDimension);
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel toolbarLeft = new JPanel();
        toolbarLeft.setLayout(new GridLayout(1, 11));
        add(toolbarLeft);

        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") +"/src/fr/virtutuile/view/ressources/save.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
            toolbarLeft.add(button);
        } catch (IOException err) {
            System.out.println(err);
        }
        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/undo.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
            toolbarLeft.add(button);
        } catch (IOException err) {
            System.out.println(err);
        }
        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/redo.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 40, 40)));
            toolbarLeft.add(button);
        } catch (IOException err) {
            System.out.println(err);
        }
        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/trash.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainWindow.controller.deleteSurface();
                }
            });
            toolbarLeft.add(button);
        } catch (IOException err) {
            System.out.println(err);
        }
        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/move.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
            button.addActionListener(handleClick(State.MOVE));
            toolbarLeft.add(button);
        } catch (IOException err) {
            System.out.println(err);
        }
        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/select.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
            button.addActionListener(handleClick(State.SELECTION));
            toolbarLeft.add(button);
        } catch (IOException err) {
            System.out.println(err);
        }
        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/arrow.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
            button.addActionListener(handleClick(State.MOVE_SURFACE));
            toolbarLeft.add(button);
        } catch (IOException err) {
            System.out.println(err);
        }
        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/grid.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainWindow.controller.switchGrid();
                }
            });
            toolbarLeft.add(button);
        } catch (IOException err) {
            System.out.println(err);
        }
        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/rect.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
            button.addActionListener(handleClick(State.CREATE_RECTANGULAR_SURFACE));
            toolbarLeft.add(button);
        } catch (IOException err) {
            System.out.println(err);
        }
        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/bind.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainWindow.controller.combineSelectedSurfaces();
                }
            });
            toolbarLeft.add(button);
        } catch (IOException err) {
            System.out.println(err);
        }


        JButton buttonAddMaterial = new JButton("+materiau");
        buttonAddMaterial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.controller.addMaterial(new Material());
                mainWindow.controller.notifyObserverForSurfaces();
            }
        });
        toolbarLeft.add(buttonAddMaterial);

        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/cut.png"));
            JButton buttonCutSurface = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
            buttonCutSurface.addActionListener(handleClick(State.CUT_SURFACE));
            toolbarLeft.add(buttonCutSurface);
        } catch (IOException err) {
            System.out.println(err);
        }
    }
}