package fr.virtutuile.view.listeners.action;

import fr.virtutuile.domain.Surface;
import fr.virtutuile.domain.VirtuTuileController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ToolBarAction extends AbstractAction {
    private Surface surface;
    private VirtuTuileController controller;


    public ToolBarAction(Surface surface, VirtuTuileController controller) {
        this.surface = surface;
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        controller.addSurface(surface);
    }
}
