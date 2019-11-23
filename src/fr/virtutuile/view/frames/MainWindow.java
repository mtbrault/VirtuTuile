package fr.virtutuile.view.frames;

import fr.virtutuile.domain.Point;
import fr.virtutuile.domain.VirtuTuileController;
import fr.virtutuile.view.menu.TopMenu;
import fr.virtutuile.view.panels.*;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainWindow extends JFrame implements KeyListener {

    private JPanel mainPanel;

    private RightPanel rightPanel;
    private CenterPanel centerPanel;
    private DrawingPanel drawingPanel;
    private FooterPanel footerPanel;
    private TopMenu topMenuPanel;
    private SideBarPanel sideBarPanel;
    private ToolBarPanel toolBarPanel;

    public VirtuTuileController controller;

    public Point mousePoint;
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        drawingPanel.notifyCreatedSurface();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    public MainWindow () {
        addKeyListener(this);
        setFocusable(true);
        controller = new VirtuTuileController();
        mousePoint = new Point();
        mainPanel = new JPanel(new BorderLayout());
        rightPanel = new RightPanel(this);
        topMenuPanel = new TopMenu(this);
        centerPanel = new CenterPanel(this);
        drawingPanel = new DrawingPanel(this);
        toolBarPanel = new ToolBarPanel(this);
        sideBarPanel = new SideBarPanel(this);
        footerPanel = new FooterPanel(this);
        addComponentListener( new ComponentListener() {
            public void componentMoved( ComponentEvent e ) {
                Point pos = new Point(drawingPanel.getLocationOnScreen().x,
                        drawingPanel.getLocationOnScreen().y);
                controller.setCanvasPosition(pos);
            }
            public void componentResized( ComponentEvent e ) {}
            public void componentShown( ComponentEvent e ) {}
            public void componentHidden( ComponentEvent e ) {}
        } );
        initWindow();
    }

    private void initWindow() {
        setTitle("VirtuTuile");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        buildGui();
    }

    private void buildGui() {

        rightPanel.add(sideBarPanel, BorderLayout.CENTER);

        centerPanel.add(drawingPanel, BorderLayout.CENTER);
        centerPanel.add(toolBarPanel, BorderLayout.NORTH);
        centerPanel.add(footerPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(topMenuPanel, BorderLayout.NORTH);
        getContentPane().add(mainPanel);
    }
}
