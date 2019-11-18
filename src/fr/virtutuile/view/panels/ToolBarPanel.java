package fr.virtutuile.view.panels;

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
        toolbarLeft.setLayout(new GridLayout(1, 9));
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
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/zoom+.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
            toolbarLeft.add(button);
        } catch (IOException err) {
            System.out.println(err);
        }
        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/zoom-.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
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
            button.addActionListener(handleClick(State.MOVE));
            toolbarLeft.add(button);
        } catch (IOException err) {
            System.out.println(err);
        }
        try {
            BufferedImage myPicture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/fr/virtutuile/view/ressources/grid.png"));
            JButton button = new JButton(new ImageIcon(resizeImage(myPicture, 60, 60)));
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
        JPanel toolbarRight = new JPanel();
        toolbarRight.setLayout(new GridLayout(1, 8, 0, 0));
        toolbarRight.setBackground(Color.yellow);
        add(toolbarRight);

        JPanel blankSpaceRight1 = new JPanel();
        blankSpaceRight1.setBackground(Color.white);
        toolbarRight.add(blankSpaceRight1);

        JPanel blankSpaceRight2 = new JPanel();
        blankSpaceRight2.setBackground(Color.white);
        toolbarRight.add(blankSpaceRight2);

        JPanel blankSpaceRight3 = new JPanel();
        blankSpaceRight3.setBackground(Color.white);
        toolbarRight.add(blankSpaceRight3);

        JPanel blankSpaceRight5 = new JPanel();
        blankSpaceRight5.setBackground(Color.white);
        toolbarRight.add(blankSpaceRight5);

        JPanel blankSpaceRight4 = new JPanel();
        blankSpaceRight4.setBackground(Color.white);
        toolbarRight.add(blankSpaceRight4);

        JPanel blankSpaceRight6 = new JPanel();
        blankSpaceRight6.setBackground(Color.white);
        toolbarRight.add(blankSpaceRight6);

        JPanel blankSpaceRight7 = new JPanel();
        blankSpaceRight7.setBackground(Color.white);
        toolbarRight.add(blankSpaceRight7);

        JPanel blankSpaceRight8 = new JPanel();
        blankSpaceRight8.setBackground(Color.white);
        toolbarRight.add(blankSpaceRight8);

        JPanel rightTool1 = new JPanel();
        rightTool1.setBackground(Color.white);
        rightTool1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        toolbarRight.add(rightTool1);

        JPanel rightTool2 = new JPanel();
        rightTool2.setBackground(Color.white);
        rightTool2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        toolbarRight.add(rightTool2);

        JPanel rightTool3 = new JPanel();
        rightTool3.setBackground(Color.white);
        rightTool3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        toolbarRight.add(rightTool3);
    }
}
