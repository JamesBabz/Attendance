/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendence.gui.controller;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public abstract class Dragable
{

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Starts the drag of the current scene.
     * @param event The mouseevent to listen for.
     */
    public void startDrag(MouseEvent event)
    {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    /**
     * Occours while the view is being dragged.
     * @param event The mouse event to listen for.
     * @param node The node on which to listen for an event.
     */
    public void dragging(MouseEvent event, Node node)
    {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

}
