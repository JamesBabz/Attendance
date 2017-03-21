/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.controller;

import attendance.be.Absence;
import attendance.be.Lecture;
import attendance.gui.model.DateTimeModel;
import attendance.gui.model.StudentModel;
import attendance.bll.PersonManager;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jacob Enemark
 */
public class StudentViewController extends Dragable implements Initializable
{

    private final StudentModel model;
    private final DateTimeModel dateTimeModel;
    private final PersonManager manager;


    @FXML
    private Label lblUser;
    @FXML
    private Button btnCheckIn;
    @FXML
    private PieChart absenceChart;
    @FXML
    private Button closeButton;

    // @FXML
    //   private ComboBox<String> comboMonth;
    @FXML
    private Label labelProcent;
    @FXML
    private HBox calendarContainer;
    @FXML
    private ImageView imageLogo;

    public StudentViewController() throws SQLException, IOException
    {
        this.manager = new PersonManager();
        this.model = StudentModel.getInstance();
//        this.absences = manager.getAllAbsence(model.getCurrentUser().getId());
        this.dateTimeModel = new DateTimeModel();


    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        lblUser.setText(model.getCurrentUser().getFirstName() + " " + model.getCurrentUser().getLastName());
        // comboMonth.setItems(dateTimeModel.getFormattedMonths());

        if (model.getCurrentUser().getLastCheckIn() != null)
        {
            if (model.getCurrentUser().getLastCheckOut() != null)
            {
                if (model.getCurrentUser().getLastCheckIn().compareTo(model.getCurrentUser().getLastCheckOut()) > 0)
                {
                    checkInStyle(false);
                }
            } else
            {
                checkInStyle(false);
            }
        }
        setLogo();
    }

    @FXML
    private void handleCheckIn() throws SQLException
    {
        checkInStyle(checkedIn());
        checkInDataHandle(checkedIn());
    }

    private void checkInDataHandle(boolean checkedIn) throws SQLException
    {
        if (checkedIn)
        {
            model.getCurrentUser().setLastCheckIn(Timestamp.valueOf(LocalDateTime.now()));
            manager.updateCheckIn(model.getCurrentUser());
        } else
        {
            model.getCurrentUser().setLastCheckOut(Timestamp.valueOf(LocalDateTime.now()));
            manager.updateCheckOut(model.getCurrentUser());
        }
    }

    private void checkInStyle(boolean checkedIn)
    {
        String btnText;
        String btnStyle;
        String loginText = "";

        if (checkedIn)
        {
            btnText = "Check-in";
            btnStyle = "-fx-background-color : LIGHTGREEN;";
        } else
        {
            btnText = "Check-out";
            btnStyle = "-fx-background-color : #FF0033;";
            loginText = ", you are now cheked-in";
        }
        btnCheckIn.setText(btnText);
        btnCheckIn.setStyle(btnStyle);
        lblUser.setText(model.getCurrentUser().getFirstName() + " " + model.getCurrentUser().getLastName() + loginText);
    }

    @FXML
    private void closeWindow()
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void drag(MouseEvent event)
    {
        dragging(event, lblUser);
    }

    @FXML
    private void setOffset(MouseEvent event)
    {
        startDrag(event);
    }

    private boolean checkedIn()
    {
        return "Check-out".equals(btnCheckIn.getText());
    }



    private void setLogo()
    {
        Image imageEasv = new Image("attendance/gui/view/images/easv.png");
        imageLogo.setImage(imageEasv);
        imageLogo.setFitHeight(80);
        imageLogo.setFitWidth(150);
    }


}
