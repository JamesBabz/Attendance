/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author sBirke
 */
public class ViewGenerator
{

    /**
     * Loads a new parentStage.
     *
     * @param parentStage The parent stage.
     * @param viewPath The view path, represented by a string.
     * @param decorated Whether the new stage being loaded should be decorated
     * or not.
     * @throws IOException
     */
    public void loadStage(Stage parentStage, String viewPath, boolean decorated) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
        Parent root = loader.load();
        parentStage.close();

        Stage newStage;
        if (decorated)
        {
            newStage = new Stage(StageStyle.DECORATED);
        }
        else
        {
            newStage = new Stage(StageStyle.UNDECORATED);
        }
        newStage.setScene(new Scene(root));

        newStage.initOwner(parentStage);

        newStage.show();
    }
}
