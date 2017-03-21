/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.controller;

import attendance.be.Absence;
import attendance.be.Lecture;
import attendance.bll.PersonManager;
import attendance.gui.model.DateTimeModel;
import attendance.gui.model.StudentModel;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thomas
 */
public class PieChartViewController implements Initializable
{

   private final ObservableList data;
    private final StudentModel model;

    List<String> lectureAbsence;
    List<Double> lectureValue;
    private final String[] DifferentClasses;

    ObservableList<PieChart.Data> pieChartData;

    @FXML
    private PieChart absenceChart;


    public PieChartViewController() throws SQLException, IOException
    {
        this.pieChartData = FXCollections.observableArrayList();
        this.model = StudentModel.getInstance();
        this.data = FXCollections.observableArrayList();
//        this.absences = manager.getAllAbsence(model.getCurrentUser().getId());
        lectureAbsence = new ArrayList<>();
        lectureValue = new ArrayList<>();

            this.DifferentClasses = new String[]
        {
            "SDE", "SCO", "ITO", "DBOS"
        };
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        updatePieChart();
      
    }



    private void updatePieChart()
    {

        updateLectureAbsence();

        absenceChart.setData(pieChartData);
        System.out.println(pieChartData);

        absenceChart.setLabelLineLength(100);
        absenceChart.setLegendSide(Side.LEFT);

//        labelProcent.setTextFill(Color.DARKORANGE);
//        labelProcent.setStyle("-fx-font: 24 arial;");
//        for (final PieChart.Data data : absenceChart.getData())
//        {
//            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
//                    new EventHandler<MouseEvent>()
//            {
//                @Override
//                public void handle(MouseEvent e)
//                {
//                    labelProcent.setTranslateX(e.getSceneX() - 180);
//                    labelProcent.setTranslateY(e.getSceneY() - 25);
//
//                    labelProcent.setText(String.valueOf(data.getPieValue()) + "%");
//
//                }
//            });
//
//        }
    }

//    private void updateLectureValue()
//    {
//        List<Lecture> lectures = model.getLectures();
//        List<Absence> missedClasses = model.getMissedClasses();
//        
//        for (Absence missedClass : missedClasses)
//        {
//            for (Lecture lecture : lectures)
//            {
//                if(lecture.getId() == missedClass.getLectureId())
//                {
//                    lectureValue.add(missedClass.)
//                }
//            }
//        }
//    }

    private void updateLectureAbsence()
    {
        List<Lecture> lectures = model.getLectures();
        List<Absence> missedClasses = model.getMissedClasses();

        for (Lecture lecture : lectures)
        {
            for (Absence missedClass : missedClasses)
            {
                if (lecture.getId() == missedClass.getLectureId())
                {
                    lectureAbsence.add(lecture.getLectureName());
                }
            }

        }
        
        getDistinct();

        for (String string : lectureAbsence)
        {
            double doubleToPrint = getAmountOfAbsencePerClass(lectureAbsence);
            System.out.println(getAmountOfAbsencePerClass(lectureAbsence));
            pieChartData.add(new PieChart.Data(string, doubleToPrint));
        }
        {

        }
    }

        private double getAmountOfAbsencePerClass(List<String> classNames)
    {
        double doubleToPrint = 0;

        for (String name : DifferentClasses)
        {
            if (Collections.frequency(classNames, name) != 0)
            {
                doubleToPrint ++; 
            }
        }
        return doubleToPrint;
    }
    private void getDistinct()
    {
        Set<String> distinctLectureAbsence = new HashSet<>(lectureAbsence);
        lectureAbsence.clear();
        lectureAbsence.addAll(distinctLectureAbsence);
//        lectureAbsence.removeAll(Collections.singleton(null));
    }
}
