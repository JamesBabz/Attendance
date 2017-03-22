/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.controller;

import attendance.be.Absence;
import attendance.be.Lecture;
import attendance.be.Semester;
import attendance.gui.model.DateTimeModel;
import attendance.gui.model.StudentModel;
import attendance.bll.PersonManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Calendar.AM;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
            }
            else
            {
                checkInStyle(false);
            }
        }
        setLogo();
        calculateAbsence();
//        try
//        {
//            manager.updateStudentImage(model.getCurrentUser(), "Hello.png");
//        }
//        catch (SQLException | IOException ex)
//        {
//            Logger.getLogger(StudentViewController.class.getName()).log(Level.SEVERE, null, ex);
//        }
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
        }
        else
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
        }
        else
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

    private void calculateAbsence()
    {
        Calendar lastCIn = Calendar.getInstance();
        lastCIn.setTimeInMillis(model.getCurrentUser().getLastCheckIn().getTime());
        Calendar lastCOut = Calendar.getInstance();
        lastCOut.setTimeInMillis(model.getCurrentUser().getLastCheckOut().getTime());

        List<Lecture> todaysLectures = getTodaysLectures();

        Calendar firstLectureStart = Calendar.getInstance();
        firstLectureStart.set(Calendar.HOUR, todaysLectures.get(0).getPeriodStart()[0]);
        firstLectureStart.set(Calendar.MINUTE, todaysLectures.get(0).getPeriodStart()[1]);

        Calendar lastLectureEnd = Calendar.getInstance();
        lastLectureEnd.set(Calendar.HOUR, todaysLectures.get(todaysLectures.size() - 1).getPeriodStart()[0]);
        lastLectureEnd.set(Calendar.MINUTE, todaysLectures.get(todaysLectures.size() - 1).getPeriodStart()[1]);

        if (!lastCIn.after(firstLectureStart) && !lastCOut.after(lastLectureEnd))
        {
            for (int i = 0; i < todaysLectures.size(); i++)
            {
                int hour = todaysLectures.get(i).getPeriodStart()[0];
                int minute = todaysLectures.get(i).getPeriodStart()[1];

                Calendar lectureStart = Calendar.getInstance();
                lectureStart.set(Calendar.AM_PM, AM);
                lectureStart.set(Calendar.HOUR, hour);
                lectureStart.set(Calendar.MINUTE, minute);
                lectureStart.set(Calendar.SECOND, 0);
                if (lastCIn.after(lectureStart))
                {
                    Absence absence = new Absence(model.getCurrentUser().getId(), todaysLectures.get(i).getId(), new Date());
                    try
                    {
                        manager.addAbsence(absence);
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(StudentViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            System.out.println("YOU HAVE ABSENCE");
        }

    }

    private List<Lecture> getTodaysLectures()
    {
        Semester semester = new Semester(new Date(), model.getCurrentUser().getClassName());
        String today = getCurrentDay();
        List<Lecture> allLectures = model.getLectures();
        List<Lecture> todaysLectures = new ArrayList<>();
        for (Lecture lecture : allLectures)
        {
            if (lecture.getDay().equals(today) && lecture.getSemester() == semester.getSemesterNum())
            {
                todaysLectures.add(lecture);
            }
        }
        Collections.sort(todaysLectures, new Comparator<Lecture>()
        {
            @Override
            public int compare(Lecture t, Lecture t1)
            {
                return t.getPeriod() - t1.getPeriod();
            }
        });
        return todaysLectures;
    }

    private String getCurrentDay()
    {
        String today;
        switch (Calendar.DAY_OF_WEEK)
        {
            case 6:
                today = "Monday";
                break;
            case 7:

                today = "Tuesday";
                break;
            case 1:

                today = "Wednesday";
                break;
            case 2:

                today = "Thursday";
                break;
            case 3:

                today = "Friday";
                break;
            default:
                today = "Weekend";
                break;
        }
        return today;
    }

}
