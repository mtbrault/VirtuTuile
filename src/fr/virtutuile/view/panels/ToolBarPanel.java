package fr.virtutuile.view.panels;

import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.Point;
import fr.virtutuile.view.Main;
import fr.virtutuile.view.frames.MainWindow;
import fr.virtutuile.view.listeners.action.ToolBarAction;

import java.awt.*;
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
    private void buildUp() {
        List<Point> points = new ArrayList<Point>();
        Surface testSurface = new Surface(points);
        Action leftTool1Action = new ToolBarAction(testSurface, mainWindow.controller);

        setLayout(new GridLayout(1, 2, 0, 0));
        Dimension toolbarDimension = getPreferredSize();
        toolbarDimension.height = 60;
        setPreferredSize(toolbarDimension);
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel toolbarLeft = new JPanel();
        toolbarLeft.setLayout(new GridLayout(1, 8, 0, 0));
        toolbarLeft.setBackground(Color.white);
        add(toolbarLeft);

        JButton button = new JButton(leftTool1Action);
        button.setBackground(Color.red);
        toolbarLeft.add(button);


        JPanel blankSpace = new JPanel();
        blankSpace.setBackground(Color.white);
        toolbarLeft.add(blankSpace);

        JPanel blankSpace2 = new JPanel();
        blankSpace2.setBackground(Color.white);
        toolbarLeft.add(blankSpace2);

        JPanel blankSpace3 = new JPanel();
        blankSpace3.setBackground(Color.white);
        toolbarLeft.add(blankSpace3);

        JPanel toolbarRight = new JPanel();
        toolbarRight.setLayout(new GridLayout(1, 8, 0, 0));
        toolbarRight.setBackground(Color.white);
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
