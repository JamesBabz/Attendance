package attendance.gui.controller;

import attendance.be.Person;
import attendance.be.Student;
import attendance.be.Teacher;
import attendance.gui.model.LoginModel;
import attendance.gui.model.StudentModel;
import attendance.gui.model.TeacherModel;
import attendance.bll.PersonManager;
import attendance.gui.model.LectureModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public class LoginViewController extends Dragable implements Initializable
{

    private final PersonManager manager;
    private final StudentModel studentModel;
    private final TeacherModel teacherModel;
    private final LectureModel lectureModel;
    private LoginModel loginModel;
    private final List<Person> people;
    private final List<Student> students;
    private final List<Teacher> teachers;
    private ViewGenerator vg;

    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtPass;
    @FXML
    private Button closeButton;
    @FXML
    private BorderPane bp;
    @FXML
    private CheckBox checkBoxRemember;
    @FXML
    private ImageView imageLogo;

    public LoginViewController() throws SQLException, IOException
    {
        this.vg = new ViewGenerator();
        this.studentModel = StudentModel.getInstance();
        this.teacherModel = TeacherModel.getInstance();
        this.lectureModel = LectureModel.getInstance();
        loginModel = LoginModel.getInstance();
        this.manager = new PersonManager();
        students = manager.getAllStudents();
        teachers = manager.getAllTeachers();
        people = manager.getAllPeople();

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        loadUserLogin();

        setCheckBoxRemember();
        setLogo();

    }

    @FXML
    private void handleLogin() throws SQLException, IOException
    {
        try
        {
        checkLoginInformation(txtUser.getText(), txtPass.getText());
        } catch (IOException ex)
        {
            showErrorDialog("I/O Error", "", "We couldn't get access to the "
                    + "requested data!");
        }
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
        dragging(event, bp);
    }

    @FXML
    private void setOffset(MouseEvent event)
    {
        startDrag(event);
    }

    /**
     * Compares the parameters provided with data from our database. If the data
     * exists the user is forwarded to a new view. The new view that is
     * displayed is dependent on the user type. (Teacher or Student)
     *
     * @param userName The user name to login with.
     * @param password The password to match the user name login.
     * @throws IOException
     */
    private void checkLoginInformation(String userName, String password) throws IOException, SQLException
    {
        for (Person person : people)
        {
            if (userName.matches(person.getUserName()) && password.matches(person.getPassword()))
            {
                if (person instanceof Teacher)
                {
                    teacherModel.setCurrentUser((Teacher) person);
                }
                else if (person instanceof Student)
                {
                    studentModel.setCurrentUser((Student) person);
                    studentModel.setMissedClasses(manager.getSingleStudentAbsence(person.getId()));
                    lectureModel.setLectures(manager.getAllLectures());
                }
                else
                {
                    return;
                }

                if (checkBoxRemember.isSelected())
                {
                    loginModel.saveLoginData(userName, password);
                }

                // A variable to hold the name of the view.
                String userType = person.getClass().getSimpleName();
                boolean isTeacher;
                if (userType.equals("Teacher"))
                {
                    isTeacher = true;
                }
                else
                {
                    isTeacher = false;
                }
                vg.loadStage((Stage) txtUser.getScene().getWindow(), "/attendance/gui/view/" + userType + "View.fxml", isTeacher);
                return;
            }
        }

        showErrorDialog("Login Error", "User not found", "Either the username or the password you provided"
                + " could not be found in our database.");
    }

    /**
     * Shows an error dialog.
     *
     * @param title The title of the error.
     * @param header The header - subtitle.
     * @param content The error message.
     */
    private void showErrorDialog(String title, String header, String content)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    /**
     * Set the checkbox if data was loaded
     */

    private void setCheckBoxRemember()
    {
        if (txtPass.getText().isEmpty())
        {
            checkBoxRemember.setSelected(false);
        }
        else
        {
            checkBoxRemember.setSelected(true);
        }
    }

    /**
     * Loads user data from file
     */
    private void loadUserLogin()
    {
        String username = "";
        String password = "";
        try
        {
            loginModel.loadLoginData();
            username = loginModel.loadLoginData()[0];
            password = loginModel.loadLoginData()[1];
        }
        catch (IOException ex)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setAlertType(AlertType.ERROR);
            alert.setHeaderText("Load failed");
        }
        if (!"".equals(username))
        {

            txtUser.setText(username);
            txtPass.setText(password);

            clearUserLogin();
        }
    }

    /**
     * Clears the user data if the checkbox is not checked
     */
    private void clearUserLogin()
    {
        if (!checkBoxRemember.isPressed())
        {
            try
            {
                loginModel.saveLoginData("", "");

            }
            catch (IOException ex)
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setAlertType(AlertType.ERROR);
                alert.setHeaderText("Save failed");
            }
        }
    }

    /**
     * Set the logo
     */
    private void setLogo()
    {
        Image imageEasv = new Image("attendance/gui/view/images/easv.png");
        imageLogo.setImage(imageEasv);
        imageLogo.setFitHeight(80);
        imageLogo.setFitWidth(150);
    }
}
