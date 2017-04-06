package attendance.gui.controller;

import attendance.be.Absence;
import attendance.be.Lecture;
import attendance.be.Semester;
import attendance.be.Student;
import attendance.gui.model.TeacherModel;
import attendance.bll.PersonManager;
import attendance.gui.model.LectureModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
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
public class TeacherViewController extends Dragable implements Initializable
{

    private static final int IMAGE_SIZE = 150;
    private final TeacherModel model;
    private final PersonManager manager;
    private final Thread imageThread;
    private final ViewGenerator vg;
    private final LectureModel lectureModel;
    private final List<Student> studentList;
    private final ObservableList<Student> allStudents;
    private final ObservableList<Student> searchedStudents;
    private final List<Absence> absence;
    private Student selectedStudent;
    private LocalDate firstDate;
    private LocalDate secondDate;

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
    @FXML
    private TableColumn<Student, Double> colAbsenceInP;
    @FXML
    private Button logoutBtn;
    @FXML
    private Label lblStudentAttendance;

    /**
     * The default constructor for the TeacherViewController.
     */
    public TeacherViewController() throws SQLException, IOException
    {
        this.lectureModel = LectureModel.getInstance();
        this.imageThread = new Thread(imageLoader());
        this.vg = new ViewGenerator();
        this.manager = new PersonManager();
        this.studentList = manager.getAllStudents();
        this.allStudents = FXCollections.observableArrayList();
        searchedStudents = FXCollections.observableArrayList();
        this.model = TeacherModel.getInstance();
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
        colAbsenceInP.setCellValueFactory(new PropertyValueFactory<>("PercentageAbsence"));
        colPictures.setCellValueFactory(new PropertyValueFactory<>("profilePic"));
        colPictures.setMinWidth(IMAGE_SIZE);
        colPictures.setMaxWidth(IMAGE_SIZE);
        colPictures.setEditable(false);

        addCheckBoxes();
        search();
        setLogo();
        setSemester();
        viewStudentsByClass();
        setClass();
        calculateAttendingStudents();

        imageThread.start();
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

    @FXML
    private void drag(MouseEvent event)
    {
        dragging(event, txtSearch);
    }

    @FXML
    private void changeStudentAbsence()
    {
        selectedStudent = tblStudentAbs.getSelectionModel().getSelectedItem();
        if (selectedStudent != null)
        {

            if (selectedStudent.isRegistered())
            {

                selectedStudent.setLastCheckOut(Timestamp.from(Instant.now()));
                selectedStudent.setLastCheckIn(null);
                selectedStudent.setRegistered(false);
                updateCheckInAndOut();
            }
            else
            {
                if (selectedStudent.getLastCheckOut() == null)
                {
                    selectedStudent.setLastCheckOut(Timestamp.from(Instant.now().minusSeconds(1)));

                    updateCheckInAndOut();
                }
                selectedStudent.setLastCheckIn(Timestamp.from(Instant.now()));
                selectedStudent.setRegistered(true);

                updateCheckInAndOut();
            }
        }
    }

    @FXML
    private void handleSemesterSelect(ActionEvent event)
    {

        if (comboClass.getSelectionModel().getSelectedIndex() != -1)
        {
            int semesterNum = comboSemester.getSelectionModel().getSelectedIndex() + 1;
            Semester semester = new Semester(semesterNum, comboClass.getValue());

            LocalDate firstDate = semester.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate secondDate = semester.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dateFirstDate.setValue(firstDate);
            dateSecondDate.setValue(secondDate);

        }

    }

    @FXML
    private void handleFirstDateSelect(ActionEvent event)
    {
        setFirstDate();
    }

    @FXML
    private void handleSecondDateSelect(ActionEvent event)
    {
        setSecondDate();
    }

    @FXML
    public void handleLogOut(ActionEvent event) throws IOException
    {
        vg.loadStage((Stage) txtSearch.getScene().getWindow(), "/attendance/gui/view/LoginView.fxml");
    }

    /**
     * Calculates num of attending students.
     */
    private void calculateAttendingStudents()
    {
        int totalStudents = studentList.size();
        int attendingStudents = 0;

        for (Student student : studentList)
        {

            if (student.isRegistered())
            {
                attendingStudents++;
            }
        }

        lblStudentAttendance.setText("" + attendingStudents + "/" + totalStudents);
    }

    /**
     * Updates the student table whenever a class is selected from the dropdown
     * menu.
     */
    private void viewStudentsByClass()
    {
        comboClass.valueProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> listener, String oldValue, String newValue)
            {
                ObservableList<Student> studentsByClassList = FXCollections.observableArrayList();
                for (Student student : allStudents)
                {
                    if (student.getClassName() != null)
                    {
                        if (student.getClassName().matches(newValue))
                        {
                            studentsByClassList.add(student);
                        }
                    }
                }

                if (comboClass.getValue().matches("All Classes"))
                {
                    tblStudentAbs.setItems(allStudents);
                }
                else
                {
                    tblStudentAbs.setItems(studentsByClassList);
                }
            }
        });
    }

    /**
     * Gets all the absence for each student.
     *
     * @param startDate the start date.
     * @param endDate the end date.
     * @throws SQLException
     */
    public void getAllAbsence(LocalDate startDate, LocalDate endDate) throws SQLException
    {
        absence.clear();
        absence.addAll(manager.getAllAbsence(startDate, endDate));
    }

    /**
     * Search and update the student table to represent people who matches the
     * search query.
     */
    private void search()
    {
        txtSearch.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> listener, String oldQuery, String newQuery)
            {
                searchedStudents.setAll(model.search(studentList, newQuery));
                tblStudentAbs.setItems(searchedStudents);
            }
        });
    }

    /**
     * Sets the EASV logo.
     */
    private void setLogo()
    {
        Image imageEasv = new Image("attendance/gui/view/images/easv.png");
        imageLogo.setImage(imageEasv);
        imageLogo.setFitHeight(80);
        imageLogo.setFitWidth(150);
    }

    /**
     * Adds a checkbox for each student in the student table view.
     */
    private void addCheckBoxes()
    {
        colAbsence.setCellValueFactory(new Callback<CellDataFeatures<Student, Boolean>, ObservableValue<Boolean>>()
        {
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<Student, Boolean> param)
            {
                return param.getValue().registeredProperty();
            }
        });

        colAbsence.setCellFactory(CheckBoxTableCell.forTableColumn(colAbsence));
    }

    /**
     * Sets the first date in the calendar.
     */
    private void setFirstDate()
    {
        firstDate = dateFirstDate.getValue();
        if (dateSecondDate.getValue() != null)
        {
            try
            {
                getAllAbsence(firstDate, secondDate);
            }
            catch (SQLException ex)
            {
                Logger.getLogger(TeacherViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Sets the second date in the calendar.
     */
    private void setSecondDate()
    {
        secondDate = dateSecondDate.getValue();
        if (dateFirstDate.getValue() != null)
        {
            try
            {
                getAllAbsence(firstDate, secondDate);
            }
            catch (SQLException ex)
            {
                Logger.getLogger(TeacherViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Loads an image for column in the student table view.
     * @return A runnable method that can be run simultaneously with the main thread.
     */
    private Runnable imageLoader()
    {
        return new Thread(() ->
        {
            int x = 0;
            Image studentImage;
            for (Student student : studentList)
            {

                if (student.getStudentImage() != null)
                {
                    studentImage = SwingFXUtils.toFXImage(student.getStudentImage(), null);
                }
                else
                {
                    studentImage = new Image("attendance/gui/view/images/profile-placeholder.png");
                }

                ImageView imgV = new ImageView(studentImage);
                imgV.setFitWidth(IMAGE_SIZE);
                imgV.setPreserveRatio(true);
                imgV.setSmooth(true);
                imgV.setCache(true);
                student.setProfilePic(imgV);
                x++;
            }            
        });
    }

    /**
     * Updates the check-in and check-out of the selected student.
     */
    private void updateCheckInAndOut()
    {
        try
        {
            manager.updateCheckIn(selectedStudent);
            manager.updateCheckOut(selectedStudent);
        }
        catch (SQLException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection to database lost");

        }
    }

    /**
     * Set the data for the semester dropdown combo.
     */
    private void setSemester()
    {
        comboSemester.getItems().addAll(
                "All Semesters",
                "1. Semester",
                "2. Semester",
                "3. Semester",
                "4. Semester",
                "5. Semester"
        );
    }

    /**
     * Sets the class the teacher is currently attending.
     * If the teacher is not attending any class at the given moment,
     * the default value 'All Classes' is selected. This represents all students
     * for all classes.
     */
    private void setClass()
    {
        comboClass.setItems(
                getAllClassNames()
        );
        for (Lecture lecture : lectureModel.getLectures())
        {
            if (TeacherModel.getInstance().getCurrentUser().getId() == lecture.getTeacherId())
            {
                if (LocalDateTime.now().getHour() == lecture.getPeriodStart()[0] && LocalDateTime.now().getMinute() >= lecture.getPeriodStart()[1] && (LocalDateTime.now().getDayOfWeek().name() == null ? lecture.getDay() == null : LocalDateTime.now().getDayOfWeek().name().equals(lecture.getDay())))
                {
                    comboClass.setValue(lecture.getLectureName());
                }
                else
                {
                    comboClass.getSelectionModel().clearAndSelect(0);
                }
            }

        }
    }

    /**
     * Gets all class names.
     * @return All class names.
     */
    private ObservableList<String> getAllClassNames()
    {
        ObservableList<String> classNames = FXCollections.observableArrayList();
        for (Student student : studentList)
        {
            if (student.getClassName() != null && !student.getClassName().contains("_")) // Just cheating to prevent unavailable classes to be displayed.
            {
                classNames.add(student.getClassName());
            }
        }

        Set<String> distinctClassNames = new HashSet<>(classNames);
        classNames.clear();
        classNames.add("All Classes");
        classNames.addAll(distinctClassNames);
        classNames.removeAll(Collections.singleton(null));
        Collections.sort(classNames);
        return classNames;
    }
}
