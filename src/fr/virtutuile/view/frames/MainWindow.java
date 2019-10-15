package fr.virtutuile.view.frames;

import fr.virtutuile.domain.VirtuTuileController;
import fr.virtutuile.view.menu.TopMenu;
import fr.virtutuile.view.panels.*;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;

public class MainWindow extends JFrame {

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

    public MainWindow() {
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
