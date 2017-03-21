/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.controller;

import attendance.be.Absence;
import attendance.be.Semester;
import attendance.be.Student;
import attendance.gui.model.DateTimeModel;
import attendance.gui.model.TeacherModel;
import attendance.bll.PersonManager;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.control.Alert;

/**
 * The controller for the teacher view.
 *
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public class TeacherViewController extends Dragable implements Initializable {

    private final TeacherModel model;
    private final PersonManager manager;
    private final DateTimeModel dateTimeModel;
    private final List<Student> studentList;

    private final ObservableList<Student> allStudents;
    private ObservableList<Student> searchedStudents;
    private final List<Absence> absence;
    private Student selectedStudent;

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
    public TeacherViewController() throws SQLException, IOException {
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
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(model.getCurrentUser().getFirstName() + " " + model.getCurrentUser().getLastName());
        tblStudentAbs.setItems(allStudents);
        colStudent.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        addCheckBoxes();

     
        updateDateInfo();
        search();
        setLogo();
        setSemester();
        setClass();
        
    }

    public void getAllAbsence(LocalDate startDate, LocalDate endDate) throws SQLException {
        absence.clear();
        absence.addAll(manager.getAllAbsence(startDate, endDate));
        for (Absence abs : absence) {
            System.out.println(abs.getDate().toString());
        }
    }

    public void handleFirstDate() throws SQLException {
        firstDate = dateFirstDate.getValue();
        if (dateSecondDate.getValue() != null) {
            getAllAbsence(firstDate, secondDate);

        }

    }

    public void handleSecondDate() throws SQLException {
        secondDate = dateSecondDate.getValue();
        if (dateFirstDate.getValue() != null) {
            getAllAbsence(firstDate, secondDate);
        }

    }

    @FXML
    private void closeWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void setOffset(MouseEvent event) {
        startDrag(event);
    }

    private void getCurrentDate() {
//        dateFirstDate.setValue(LocalDate.now());
//        dateSecondDate.setValue(LocalDate.now());

    }

    private void updateDateInfo() {

    }

    private void search() {
        txtSearch.textProperty().addListener((ObservableValue<? extends String> listener, String oldQuery, String newQuery)
                -> {
            searchedStudents.setAll(model.search(studentList, newQuery));
            tblStudentAbs.setItems(searchedStudents);
        });
    }

    private void setLogo() {
        Image imageEasv = new Image("attendance/gui/view/images/easv.png");
        imageLogo.setImage(imageEasv);
        imageLogo.setFitHeight(80);
        imageLogo.setFitWidth(150);
    }

    @FXML
    private void drag(MouseEvent event) {
        dragging(event, txtSearch);
    }

    private void addCheckBoxes() {

        colAbsence.setCellValueFactory(
                new Callback<CellDataFeatures<Student, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<Student, Boolean> param) {
                return param.getValue().registeredProperty();
            }
        });

        colAbsence.setCellFactory(CheckBoxTableCell.forTableColumn(colAbsence));
    }

    @FXML
    private void changeStudentAbsence() {
        selectedStudent = tblStudentAbs.getSelectionModel().getSelectedItem();

        if (selectedStudent.isRegistered()) {

            selectedStudent.setLastCheckOut(Timestamp.from(Instant.now()));
            selectedStudent.setLastCheckIn(null);
            selectedStudent.setRegistered(false);
            updateCheckInAndOut();
        } else {
            if (selectedStudent.getLastCheckOut() == null) {
                selectedStudent.setLastCheckOut(Timestamp.from(Instant.now().minusSeconds(1)));

                updateCheckInAndOut();
            }
            selectedStudent.setLastCheckIn(Timestamp.from(Instant.now()));
            selectedStudent.setRegistered(true);

            updateCheckInAndOut();
        }
    }

    private void updateCheckInAndOut() {
        try {
            manager.updateCheckIn(selectedStudent);
            manager.updateCheckOut(selectedStudent);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection to database lost");

        }
    }

    private void setSemester() {

        comboSemester.getItems().addAll(
                "1. Semester",
                "2. Semester",
                "3. Semester",
                "4. Semester"
        );
    }

    private void setClass() {
        comboClass.getItems().addAll(
                getAllClassNames()
        );
    }

    private List<String> getAllClassNames() {
        List<String> classNames = new ArrayList<>();
        for (Student student : studentList) {
            classNames.add(student.getClassName());
        }

        Set<String> distinctClassNames = new HashSet<>(classNames);
        classNames.clear();
        classNames.addAll(distinctClassNames);
        classNames.removeAll(Collections.singleton(null));
        Collections.sort(classNames);
        return classNames;
    }

    @FXML
    private void handleSemesterSelect(ActionEvent event) {
        
        if(comboClass.getSelectionModel().getSelectedIndex() != -1){
        int semesterNum = comboSemester.getSelectionModel().getSelectedIndex() + 1;
        Semester semester = new Semester(semesterNum, comboClass.getValue());
        
        LocalDate firstDate = semester.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate secondDate = semester.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        dateFirstDate.setValue(firstDate);
        dateSecondDate.setValue(secondDate);

        }

  
    }

}
