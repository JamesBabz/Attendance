/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.controller;

import attendance.be.Absence;
import attendance.be.Lecture;
import attendance.be.Semester;
import attendance.be.Student;
import attendance.gui.model.DateTimeModel;
import attendance.gui.model.StudentModel;
import attendance.bll.PersonManager;
import attendance.gui.model.LectureModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Jacob Enemark
 */
public class StudentViewController extends Dragable implements Initializable
{

    private final StudentModel studentModel;
    private final LectureModel lectureModel;
    private final DateTimeModel dateTimeModel;
    private final PersonManager manager;
    private final Student currentUser;

    @FXML
    private Label lblUser;
    @FXML
    private Button btnCheckIn;
    @FXML
    private PieChart absenceChart;
    @FXML
    private Button closeButton;
    @FXML
    private Label labelProcent;
    @FXML
    private HBox calendarContainer;
    @FXML
    private ImageView imageLogo;

    public StudentViewController() throws SQLException, IOException
    {
        this.manager = new PersonManager();
        this.studentModel = StudentModel.getInstance();
        this.lectureModel = LectureModel.getInstance();
        this.dateTimeModel = new DateTimeModel();
        this.currentUser = studentModel.getCurrentUser();

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        if (currentUser.getLastCheckIn() != null && currentUser.getLastCheckOut() != null)
        {
            if (isLastCheckOutWrong())
            {
                editWrongCheckOut();
            }
        }
        giveAbsenceWhenSick();
        try
        {
            studentModel.setMissedClasses(manager.getSingleStudentAbsence(currentUser.getId()));
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StudentViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        lblUser.setText(currentUser.getFirstName() + " " + currentUser.getLastName());

        if (currentUser.getLastCheckIn() != null)
        {
            if (currentUser.getLastCheckOut() != null)
            {
                if (currentUser.getLastCheckIn().compareTo(currentUser.getLastCheckOut()) > 0)
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
            currentUser.setLastCheckIn(Timestamp.valueOf(LocalDateTime.now()));
            manager.updateCheckIn(currentUser);
        }
        else
        {
            currentUser.setLastCheckOut(Timestamp.valueOf(LocalDateTime.now()));
            manager.updateCheckOut(currentUser);
            calculateTodaysAbsence();
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
        lblUser.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
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

    private Calendar getSchoolEnd(List<Lecture> todaysLectures)
    {
        Calendar lastLectureEnd = Calendar.getInstance();
        lastLectureEnd.set(Calendar.HOUR, todaysLectures.get(todaysLectures.size() - 1).getPeriodStart()[0]);
        lastLectureEnd.set(Calendar.MINUTE, todaysLectures.get(todaysLectures.size() - 1).getPeriodStart()[1]);
        return lastLectureEnd;
    }

    private Calendar getSchoolStart(List<Lecture> todaysLectures)
    {
        Calendar firstLectureStart = Calendar.getInstance();
        firstLectureStart.set(Calendar.HOUR, todaysLectures.get(0).getPeriodStart()[0]);
        firstLectureStart.set(Calendar.MINUTE, todaysLectures.get(0).getPeriodStart()[1]);
        return firstLectureStart;
    }

    private List<Lecture> getTodaysLectures()
    {
        return getADaysLectures(Calendar.getInstance());
    }

    private List<Lecture> getADaysLectures(Calendar now)
    {
        Semester semester = new Semester(new Date(), currentUser.getClassName());
        String today = getCurrentDay(now);
        List<Lecture> allLectures = lectureModel.getLectures();
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

    private String getCurrentDay(Calendar now)
    {
        String today;
        switch (now.get(Calendar.DAY_OF_WEEK))
        {
            case 2:
                today = "Monday";
                break;
            case 3:
                today = "Tuesday";
                break;
            case 4:
                today = "Wednesday";
                break;
            case 5:
                today = "Thursday";
                break;
            case 6:
                today = "Friday";
                break;
            default:
                today = "Weekend";
                break;
        }
        return today;
    }

    /**
     * Changes view to login screen, whenever the Log-out button is pressed
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void handleLogOut(ActionEvent event) throws IOException
    {
        goLoginScreen("/attendance/gui/view/LoginView.fxml");
    }

    /**
     * Set the scene to login-view.
     *
     * @param viewPath
     * @throws IOException
     */
    private void goLoginScreen(String viewPath) throws IOException
    {
        Stage primaryStage = (Stage) lblUser.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
        Parent root = loader.load();
        primaryStage.close();

        Stage newStage = new Stage(StageStyle.UNDECORATED);
        newStage.setScene(new Scene(root));

        newStage.initOwner(primaryStage);

        newStage.show();
    }

    private boolean isLastCheckOutWrong()
    {

        // Check if forgotten to check out
        if (currentUser.getLastCheckIn().after(currentUser.getLastCheckOut()))
        {
            return true;
        }
        return false;
    }

    private void editWrongCheckOut()
    {
        Calendar calNow, calCI, calCO;
        calNow = Calendar.getInstance();
        calCI = Calendar.getInstance();
        calCO = Calendar.getInstance();

        Timestamp lastCI = currentUser.getLastCheckIn();
        calCI.setTime(lastCI);

        Timestamp lastCO = currentUser.getLastCheckOut();
        calCO.setTime(lastCO);

        Calendar newCO = calCI;
        newCO.set(Calendar.HOUR_OF_DAY, 23);
        newCO.set(Calendar.MINUTE, 59);
        Timestamp coMili = new Timestamp(newCO.getTimeInMillis());
        currentUser.setLastCheckOut(coMili);
        try
        {
            manager.updateCheckOut(currentUser);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StudentViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void giveAbsenceWhenSick()
    {
        Calendar calNow = Calendar.getInstance();
        Calendar calCO = Calendar.getInstance();
        Timestamp lastCO;
        lastCO = currentUser.getLastCheckOut();
        calCO.setTime(lastCO);
        if (calNow.get(Calendar.DAY_OF_YEAR) > calCO.get(Calendar.DAY_OF_YEAR) + 1)
        {
            int remainingDays = calNow.get(Calendar.DAY_OF_YEAR) - calCO.get(Calendar.DAY_OF_YEAR);
            for (int i = 1; i < remainingDays; i++)
            {
                Calendar current = calCO;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                current.add(Calendar.DAY_OF_YEAR, 1);

                while (current.get(Calendar.DAY_OF_WEEK) == 7 || current.get(Calendar.DAY_OF_WEEK) == 1)
                {
                    current.add(Calendar.DAY_OF_YEAR, 1);
                    i++;
                }
                if (i == remainingDays)
                {
                    break;
                }
                for (Lecture lecture : getADaysLectures(current))
                {
                    if (isAbsenceUnique(lecture, sdf, current))
                    {
                        try
                        {
                            Absence absence = new Absence(currentUser.getId(), lecture.getId(), current.getTime());
                            manager.addAbsence(absence);
                        }
                        catch (SQLException ex)
                        {
                            Logger.getLogger(StudentViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }

    private boolean isAbsenceUnique(Lecture lecture, SimpleDateFormat sdf, Calendar current)
    {
        boolean isUnique = true;
        for (Absence abs : studentModel.getMissedClasses())
        {
            if (abs.getStudentId() == currentUser.getId() && abs.getLectureId() == lecture.getId() && abs.getDate().toString().equals(sdf.format(current.getTime())))
            {
                isUnique = false;
            }
        }
        return isUnique;
    }

    private void calculateTodaysAbsence() throws SQLException
    {
        Timestamp checkIn = currentUser.getLastCheckIn();
        Timestamp checkOut = currentUser.getLastCheckOut();
        Calendar schoolStart, schoolEnd, lectStart, lectEnd;
        schoolStart = getSchoolStart(getTodaysLectures());
        schoolEnd = getSchoolEnd(getTodaysLectures());
        lectStart = Calendar.getInstance();
        lectEnd = Calendar.getInstance();
        int checkInPeriod = getTodaysLectures().get(0).getPeriod();
        int checkOutPeriod = getTodaysLectures().get(getTodaysLectures().size() - 1).getPeriod();
        int firstPeriod = getTodaysLectures().get(0).getPeriod();
        int lastPeriod = getTodaysLectures().get(getTodaysLectures().size() - 1).getPeriod();
        for (Lecture lecture : getTodaysLectures())
        {
            lectStart.set(Calendar.HOUR_OF_DAY, lecture.getPeriodStart()[0]);
            lectStart.set(Calendar.MINUTE, lecture.getPeriodStart()[1]);
            lectEnd.set(Calendar.HOUR_OF_DAY, lecture.getPeriodEnd()[0]);
            lectEnd.set(Calendar.MINUTE, lecture.getPeriodEnd()[1]);
            if (checkIn.getTime() > lectStart.getTimeInMillis())
            {
                checkInPeriod = lecture.getPeriod();
                if (checkInPeriod > firstPeriod)
                {
                    Absence absence = new Absence(currentUser.getId(), lecture.getId(), new Date());
                    manager.addAbsence(absence);
                }
            }
            if (checkOut.getTime() < lectEnd.getTimeInMillis())
            {

                Absence absence = new Absence(currentUser.getId(), lecture.getId(), new Date());
                manager.addAbsence(absence);
            }
        }

    }

}
