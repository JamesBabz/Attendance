/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendence.gui.controller;

import attendence.be.Absence;
import attendence.be.Semester;
import attendence.gui.model.StudentModel;
import java.net.URL;
import java.util.Calendar;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author James
 */
public class CalendarViewController implements Initializable
{

    private Calendar cal;
    private int year;
    private int month;
    private final String todayStyle;

    private final String absentStyle;
    private final String attendetStyle;
    private final StudentModel model;
    private ObservableList<String> months;
    private ObservableList<Integer> years;

    @FXML
    private GridPane gridCalendar;
    @FXML
    private ComboBox<String> cmbMonth;
    @FXML
    private ComboBox<Integer> cmbYear;

    public CalendarViewController()
    {
        this.attendetStyle = "-fx-background-color: lightgreen";
        this.absentStyle = "-fx-background-color: #FF0033";
        this.todayStyle = "-fx-border-color: red;";
        this.model = StudentModel.getInstance();
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
        cmbMonth.getSelectionModel().select(Calendar.MONTH);
        cmbYear.setItems(years);
        cmbYear.getSelectionModel().select(0);
    }

    private void fillCalendar()
    {
        gridCalendar.getChildren().remove(5, gridCalendar.getChildren().size()); // Clears everything except the weekdays
        cal.set(year, month, 1); // Sets the month/year for the calendar to show
        String dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH); // Gets the shortened name for the day
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // Gets number of days in the selected month
        Date firstSemester = new Semester(1, model.getCurrentUser().getClassName()).getStartDate();
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
                Button btn = new Button(i + "");
                btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                Calendar tempCal = Calendar.getInstance();
                if (i == tempCal.get(Calendar.DATE) && month == tempCal.get(Calendar.MONTH) && year == tempCal.get(Calendar.YEAR))
                {
                    btn.setStyle(todayStyle);
                }
                if (month < tempCal.get(Calendar.MONTH) || i < tempCal.get(Calendar.DATE) && month == tempCal.get(Calendar.MONTH) || year < tempCal.get(Calendar.YEAR))
                {

                    if (cal.getTime().after(firstSemester))
                    {
                        checkIfAbsent(i, btn);
                    }
                }
                gridCalendar.add(btn, gridX, gridY);
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

    private void checkIfAbsent(int i, Button btn)
    {
        List<Absence> missedClasses = model.getMissedClasses();
        if(missedClasses.isEmpty())
        {
            btn.setStyle(attendetStyle);
        }
        for (Absence missedClass : missedClasses)
        {
            Calendar missCal = Calendar.getInstance();
            missCal.setTime(missedClass.getDate());
            if (missCal.get(Calendar.DATE) == i && month == missCal.get(Calendar.MONTH) && year == missCal.get(Calendar.YEAR))
            {
                btn.setStyle(absentStyle);
                return;
            }
            else
            {
                btn.setStyle(attendetStyle);
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
}