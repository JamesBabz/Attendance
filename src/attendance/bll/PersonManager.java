package attendance.bll;

import attendance.be.Absence;
import attendance.be.Lecture;
import attendance.be.Person;
import attendance.be.Student;
import attendance.be.Teacher;
import attendance.dal.PersonDAO;
import attendance.dal.DBManager;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * A class that holds data about the people.
 *
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public class PersonManager {

    PersonDAO personDAO = PersonDAO.getInstance();
    DBManager dbManager;

    public PersonManager() throws SQLException, IOException {
        this.dbManager = new DBManager();
    }

    /**
     * Gets all the students.
     *
     * @return
     */
    public List<Student> getAllStudents() {
        return dbManager.getStudents();
    }

    /**
     * Gets all the teachers.
     *
     * @return
     */
    public List<Teacher> getAllTeachers() {
        return dbManager.getTeachers();
    }

    /**
     * Gets the absence for each person.
     *
     * @param sID
     * @return
     */
    public List<Absence> getSingleStudentAbsence(int sID) throws SQLException {
        return dbManager.getSingleStudentAbsence(sID);
    }

    public List<Absence> getAllAbsence(LocalDate startDate, LocalDate endDate) throws SQLException {
        return dbManager.getAllAbsence(startDate, endDate);
    }

    /**
     * Gets all the people (Teachers and students)
     *
     * @return
     */
    public List<Person> getAllPeople() {
        return dbManager.getPeople();
    }

    public void updateCheckIn(Student student) throws SQLException {
        dbManager.updateCheckIn(student);
    }

    public void updateCheckOut(Student student) throws SQLException {
        dbManager.updateCheckOut(student);
    }
    
    public List<Lecture> getAllLectures(){
        return dbManager.getAllLectures();
    }
    
    public void addAbsence(Absence absence) throws SQLException{
        dbManager.addAbsence(absence);
    }

}
