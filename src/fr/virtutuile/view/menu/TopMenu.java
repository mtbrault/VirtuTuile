package fr.virtutuile.view.menu;

import fr.virtutuile.view.frames.MainWindow;

import javax.swing.*;

public class TopMenu extends JMenuBar {

    public TopMenu(MainWindow outer) {
        buildUp();
    }

    private void buildUp() {
        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem fileChoose = new JMenuItem("File...");
        file.add(fileChoose);
        file.add(exit);
        add(file);
    }
}
