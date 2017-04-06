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

    private Stage stage;

    public void loadStage(Stage stage, String viewPath, boolean decorated) throws IOException
    {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
        Parent root = loader.load();
        stage.close();
        
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

        newStage.initOwner(stage);

        newStage.show();
    }

    public Stage getStage()
    {
        return stage;
    }

}
