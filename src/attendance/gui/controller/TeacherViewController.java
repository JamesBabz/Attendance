/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.controller;

import attendance.be.Absence;
import attendance.be.Student;
import attendance.gui.model.DateTimeModel;
import attendance.gui.model.TeacherModel;
import attendence.bll.PersonManager;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * The controller for the teacher view.
 *
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public class TeacherViewController extends Dragable implements Initializable
{

    private final TeacherModel model;
    private final PersonManager manager;
    private final DateTimeModel dateTimeModel;
    private final List<Student> studentList;
    private final ObservableList<Student> allStudents;
    private ObservableList<Student> searchedStudents;
    private final List<Absence> absence;


    @FXML
    private Label lblUsername;
    @FXML
    private TableView<Student> tblStudentAbs;
    @FXML
    private TableColumn<Student, String> colStudent;
    @FXML
    private TableColumn<Student, Boolean> colAbsence;
    @FXML
    private Button closeButton;
    @FXML
    private ComboBox<String> comboClass;
    @FXML
    private ComboBox<String> comboSemester;
    @FXML
    private TableColumn<Student, Image> colPictures;
    @FXML
    private TextField txtSearch;
    @FXML
    private ImageView imageLogo;
    @FXML
    private DatePicker dateFirstDate;
    @FXML
    private DatePicker dateSecondDate;
    
    LocalDate firstDate;
    LocalDate secondDate;

    /**
     * The default constructor for the TeacherViewController.
     */
    public TeacherViewController() throws SQLException, IOException
    {
        this.manager = new PersonManager();
        this.studentList = manager.getAllStudents();
        this.allStudents = FXCollections.observableArrayList();
        searchedStudents = FXCollections.observableArrayList();
        this.model = TeacherModel.getInstance();
        dateTimeModel = new DateTimeModel();
        allStudents.addAll(studentList);
        absence = new ArrayList<>();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        lblUsername.setText(model.getCurrentUser().getFirstName() + " " + model.getCurrentUser().getLastName());
        tblStudentAbs.setItems(allStudents);
        colStudent.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        addCheckBoxes();

        fillComboBoxes();
        updateDateInfo();
        search();
        setLogo();
        

    }

       public void getAllAbsence(LocalDate startDate, LocalDate endDate) throws SQLException {
        absence.clear();
        absence.addAll(manager.getAllAbsence(startDate, endDate));
        for (Absence abs : absence) {
                System.out.println(abs.getDate().toString());
            }
    }

    @FXML
    public void handleFirstDate() throws SQLException {
        firstDate = dateFirstDate.getValue();
        if (dateSecondDate.getValue() != null) {
            getAllAbsence(firstDate, secondDate);
            
        }

    }

    @FXML
    public void handleSecondDate() throws SQLException {
        secondDate = dateSecondDate.getValue();
        if (dateFirstDate.getValue() != null) {
            getAllAbsence(firstDate, secondDate);
        }

    }
    
    
    @FXML
    private void closeWindow(MouseEvent event)
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void setOffset(MouseEvent event)
    {
        startDrag(event);
    }

    private void fillComboBoxes()
    {
        comboClass.getItems().add("CS2016A");
        comboClass.getItems().add("CS2016B");

        getCurrentDate();
    }

    private void getCurrentDate()
    {
//        dateFirstDate.setValue(LocalDate.now());
//        dateSecondDate.setValue(LocalDate.now());

    }

    private void updateDateInfo()
    {

    }

    private void search()
    {
        txtSearch.textProperty().addListener((ObservableValue<? extends String> listener, String oldQuery, String newQuery)
                -> 
                {
                    searchedStudents.setAll(model.search(studentList, newQuery));
                    tblStudentAbs.setItems(searchedStudents);
        });
    }

    private void setLogo()
    {
        Image imageEasv = new Image("attendance/gui/view/images/easv.png");
        imageLogo.setImage(imageEasv);
        imageLogo.setFitHeight(80);
        imageLogo.setFitWidth(150);
    }

    @FXML
    private void drag(MouseEvent event)
    {
        dragging(event, txtSearch);
    }

    private void addCheckBoxes()
    {
        colAbsence.setCellValueFactory(
                new Callback<CellDataFeatures<Student, Boolean>, ObservableValue<Boolean>>()
        {

            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<Student, Boolean> param)
            {
                return param.getValue().registeredProperty();
            }
        });

        colAbsence.setCellFactory(CheckBoxTableCell.forTableColumn(colAbsence));
    }
}
