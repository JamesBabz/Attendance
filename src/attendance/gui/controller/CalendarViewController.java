/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.controller;

import attendance.be.Absence;
import attendance.be.Lecture;
import attendance.be.Semester;
import attendance.bll.PersonManager;
import attendance.gui.model.LectureModel;
import attendance.gui.model.StudentModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Tooltip;

/**
 * FXML Controller class
 *
 * @author James
 */
public class CalendarViewController implements Initializable
{

    private Calendar cal;
    private int year;
    private static int  month;
    private final String todayStyle;
    private final String absentStyle;
    private final String attendetStyle;
    private final StudentModel studentModel;
    private final LectureModel lectureModel;
    private final PersonManager manager;
    private final ObservableList<String> months;
    private final ObservableList<Integer> years;
    private final String[] DifferentClasses;

    @FXML
    private GridPane gridCalendar;
    @FXML
    private ComboBox<String> cmbMonth;
    @FXML
    private ComboBox<Integer> cmbYear;

    public CalendarViewController() throws SQLException, IOException
    {
        this.DifferentClasses = new String[]
        {
            "SDE", "SCO", "ITO", "DBOS"
        };
        this.attendetStyle = "-fx-background-color: lightgreen";
        this.absentStyle = "-fx-background-color: #FF0033";
        this.todayStyle = "-fx-border-color: red;";
        this.studentModel = StudentModel.getInstance();
        this.lectureModel = LectureModel.getInstance();
        this.manager = new PersonManager();
        this.cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        months = FXCollections.observableArrayList(
                "January",
                "Febuary",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        );
        years = FXCollections.observableArrayList(
                year,
                year - 1,
                year - 2,
                year - 3,
                year - 4
        );
       
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        fillCalendar();
        cmbMonth.setItems(months);
        cmbMonth.getSelectionModel().select(month);
        cmbYear.setItems(years);
        cmbYear.getSelectionModel().select(0);
    }

    public void fillCalendar()
    {
        gridCalendar.getChildren().remove(5, gridCalendar.getChildren().size()); // Clears everything except the weekdays
        cal.set(year, month, 1); // Sets the month/year for the calendar to show
        String dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH); // Gets the shortened name for the day
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // Gets number of days in the selected month
        Date firstSemester = new Semester(1, studentModel.getCurrentUser().getClassName()).getStartDate();
        int gridX = 0;
        int gridY = 1;
        switch (dayOfWeek)
        {
            case "Mon":
                gridX = 0;
                break;
            case "Tue":
                gridX = 1;
                break;
            case "Wed":
                gridX = 2;
                break;
            case "Thu":
                gridX = 3;
                break;
            case "Fri":
                gridX = 4;
                break;
            case "Sat":
                gridX = 5;
                break;
            case "Sun":
                gridX = 6;
                break;
            default:
                gridX = 0;
                break;
        }

        // Loops through the grid placing numbers equal to the amount of days in the month
        for (int i = 1; i < daysInMonth + 1; i++)
        {

            if (gridX <= 4)
            {
                Label label = new Label(i + "");
                label.setAlignment(Pos.CENTER);
                label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                Calendar tempCal = Calendar.getInstance();
                if (i == tempCal.get(Calendar.DATE) && month == tempCal.get(Calendar.MONTH) && year == tempCal.get(Calendar.YEAR))
                {
                    label.setStyle(todayStyle);
                }
                if (month < tempCal.get(Calendar.MONTH) || i < tempCal.get(Calendar.DATE) && month == tempCal.get(Calendar.MONTH) || year < tempCal.get(Calendar.YEAR))
                {

                    if (cal.getTime().after(firstSemester))
                    {
                        checkIfAbsent(i, label);
                    }
                }
                gridCalendar.add(label, gridX, gridY);
            }

            // Next row and back to the first column after sunday
            if (gridX == 6)
            {
                gridX = 0;
                gridY++;
            }
            else
            {
                gridX++;
            }
        }
    }

    private void checkIfAbsent(int i, Label label)
    {
        List<Absence> missedClasses = studentModel.getMissedClasses();
        List<Lecture> missedLectures = lectureModel.getLectures();
        List<String> classNames = new ArrayList<>();;

        if (missedClasses.isEmpty())
        {
            label.setStyle(attendetStyle);

        }
        else
        {
            boolean isAbsent = false;
            for (Absence missedClass : missedClasses)
            {
                String stringToPrint;
                Calendar missCal = Calendar.getInstance();
                missCal.setTime(missedClass.getDate());
                if (missCal.get(Calendar.DATE) == i && month == missCal.get(Calendar.MONTH) && year == missCal.get(Calendar.YEAR))
                {
                    label.setStyle(absentStyle);
                    for (Lecture lecture : missedLectures)
                    {
                        if (lecture.getId() == missedClass.getLectureId())
                        {
                            classNames.add(lecture.getLectureName());
                        }
                    }
                    stringToPrint = getAmountOfAbsencePerClass(classNames);

                    Tooltip.install(label, new Tooltip(stringToPrint));
                    isAbsent = true;
                }
                else if (!isAbsent)
                {
                    label.setStyle(attendetStyle);
                }
            }
        }
    }

    @FXML
    private void changeYear(ActionEvent event)
    {
        year = cmbYear.getValue();
        fillCalendar();

    }

    @FXML
    private void changeMonth(ActionEvent event)
    {
        String selected = cmbMonth.getValue();
        for (int i = 0; i < months.size(); i++)
        {
            if (selected.equals(months.get(i)))
            {
                month = i;
            }
        }
        fillCalendar();
    }

    private String getAmountOfAbsencePerClass(List<String> classNames)
    {
        String stringToPrint = "";

        for (String name : DifferentClasses)
        {
            if (Collections.frequency(classNames, name) != 0)
            {
                stringToPrint += name + " (" + Collections.frequency(classNames, name) + ")\n";
            }
        }
        return stringToPrint;
    }
    
    public void setMonth()
    {
        studentModel.setMonth(month);
    }
}
