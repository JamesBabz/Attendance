/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.controller;

import attendance.be.Absence;
import attendance.be.Lecture;
import attendance.gui.model.LectureModel;
import attendance.gui.model.StudentModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author thomas
 */
public class PieChartViewController implements Initializable
{

    private final ObservableList data;
    private final StudentModel studentModel;
    private final LectureModel lectureModel;

    List<String> lectureAbsence;
    List<Double> lectureValue;
    private final String[] DifferentClasses;

    ObservableList<PieChart.Data> pieChartData;

    @FXML
    private PieChart absenceChart;
    @FXML
    public Label lblProcent;
    @FXML
    private Label lblMonth;

    public PieChartViewController() throws SQLException, IOException
    {
        this.pieChartData = FXCollections.observableArrayList();

        this.studentModel = StudentModel.getInstance();
        this.lectureModel = LectureModel.getInstance();
        this.data = FXCollections.observableArrayList();

        lectureAbsence = new ArrayList<>();
        lectureValue = new ArrayList<>();

        this.DifferentClasses = new String[]
        {
            "DBOS", "ITO", "SCO", "SDE"
        };

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        absenceChart.setData(pieChartData);
        updatePieChart();

    }

    /**
     * Updates the piechart and holds the listener for the absence % when clicked
     */
    public void updatePieChart()
    {

        updateLectureAbsence();

        lblMonth.setText("Absence in " + getMonth());
        GetProcentToPieChart();
    }
    /**
     * Gets the month to be shown in the title of the pie chart.
     * @return the month that has been selected
     */
    private String getMonth()
    {
        String month = "";
        switch (studentModel.getMonth())
        {
            case 0:
                month = "January";
                break;
            case 1:
                month = "Febuary";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            case 11:
                month = "December";
                break;
            default:
                break;
        }
   
        return month;
    }


/**
 * Shows the procent when the pie chart is being clicked.
 */
    private void GetProcentToPieChart()
    {
        lblProcent.setTextFill(Color.BLACK);
        lblProcent.setStyle("-fx-font: 16 arial;");

        for (final PieChart.Data data : absenceChart.getData())
        {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent e)
                {
                    DoubleBinding total = Bindings.createDoubleBinding(()
                            -> pieChartData.stream().collect(Collectors.summingDouble(PieChart.Data::getPieValue)), pieChartData);

                    String text = String.format("%.1f%%", 100 * data.getPieValue() / total.get());
                    lblProcent.setTranslateX(e.getX());
                    lblProcent.setTranslateY(e.getY() -17);
                    lblProcent.setText(text);
                }
            });
        }
    }

    /**
     * Updates the data in the piechart when a month/year is selected
     */
    private void updateLectureAbsence()
    {
        pieChartData.clear();
        lectureAbsence.clear();
        List<Lecture> lectures = lectureModel.getLectures();
        List<Absence> missedClasses = studentModel.getMissedClasses();
        for (Lecture lecture : lectures)
        {
            for (Absence missedClass : missedClasses)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(missedClass.getDate());
                if (cal.get(Calendar.MONTH) == studentModel.getMonth() && cal.get(Calendar.YEAR) == studentModel.getYear() && lecture.getId() == missedClass.getLectureId())
                {
                    lectureAbsence.add(lecture.getLectureName());

                }
            }
        }

        double[] absence = getAmountOfAbsencePerClass(lectureAbsence);
        pieChartData.add(new PieChart.Data("Attendance", getMonthLectures()));

        for (int i = 0; i < getDistinct().size(); i++)
        {
            pieChartData.add(new PieChart.Data(getDistinct().get(i) + " Absence", absence[i]));
        }

    }

    /**
     * Gets the class name and amount of the absence current month 
     * @param classNames - All the class names
     * @return 
     */
    private double[] getAmountOfAbsencePerClass(List<String> classNames)
    {
        double[] amount = new double[getDistinct().size()];
        String txt = "";
        for (String name : DifferentClasses)
        {
            if (Collections.frequency(classNames, name) != 0)
            {
                txt += Collections.frequency(classNames, name) + ",";
            }
        }
        String[] txtArray = txt.split(",");
        for (int i = 0; i < amount.length; i++)
        {
            amount[i] = Double.parseDouble(txtArray[i]);
        }
        return amount;
    }

    /**
     * Gets amount of distinct lectures
     * @return  - distinct lectures
     */
    private ArrayList<String> getDistinct()
    {
        ArrayList<String> returnArray = new ArrayList<>();
        Set<String> distinctLectureAbsence = new HashSet<>(lectureAbsence);
        returnArray.clear();
        returnArray.addAll(distinctLectureAbsence);
        returnArray.removeAll(Collections.singleton(null));
        Collections.sort(returnArray);
        return returnArray;
    }

    /**
     * Get lectuers of each month
     *
     * @return - amount of lectures
     */
    private int getMonthLectures()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        Calendar dayInWeek = Calendar.getInstance();
        dayInWeek.set(Calendar.MONTH, studentModel.getMonth() + 1);
        int daysInMonth = dayInWeek.getActualMaximum(Calendar.DAY_OF_MONTH);
        int lecturesInMonth = 0;
        for (int i = 1; i < daysInMonth; i++)
        {
            dayInWeek.set(Calendar.DAY_OF_MONTH, i);
            String today;
            switch (dayInWeek.get(Calendar.DAY_OF_WEEK))
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
            for (Lecture lecture : lectureModel.getLectures())
            {
                if (today.equals(lecture.getDay()))
                {
                    lecturesInMonth++;

                }
            }

        }

        return lecturesInMonth;
    }
}
