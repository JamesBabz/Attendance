package attendance.bll;

import attendance.be.Absence;
import attendance.be.Lecture;
import attendance.be.Person;
import attendance.be.Student;
import attendance.be.Teacher;
import attendance.dal.DBManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * A class that holds data about the people.
 *
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public class PersonManager
{

    DBManager dbManager;

    /**
     * The constuctor for the class.
     * @throws SQLException
     * @throws IOException
     */
    public PersonManager() throws SQLException, IOException
    {
        this.dbManager = new DBManager();
    }

    /**
     * 
     * Gets all the students.
     *
     * @return - List of students
     */
    public List<Student> getAllStudents()
    {
        return dbManager.getStudents();
    }

    /**
     * 
     * Gets all the teachers.
     *
     * @return - List of teachers
     */
    public List<Teacher> getAllTeachers()
    {
        return dbManager.getTeachers();
    }

    /**
     * 
     * Gets the absence for a single person.
     *
     * @param sID - The student ID
     * @return - List of absence the student has
     * @throws java.sql.SQLException
     */
    public List<Absence> getSingleStudentAbsence(int sID) throws SQLException
    {
        return dbManager.getSingleStudentAbsence(sID);
    }

    /**
     * Gets all absence.
     * @param startDate - The from date for retrieving absence
     * @param endDate - The to date for retrieving absence
     * @return - List of absence between two periods
     * @throws SQLException 
     */
    public List<Absence> getAllAbsence(LocalDate startDate, LocalDate endDate) throws SQLException
    {
        return dbManager.getAllAbsence(startDate, endDate);
    }

    /**
     * Gets all the people (Teachers and students)
     *
     * @return - List of all people
     */
    public List<Person> getAllPeople()
    {
        return dbManager.getPeople();
    }

    /**
     * @param student
     * @param imagePath
     * @throws SQLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void updateStudentImage(Student student, String imagePath) throws SQLException, FileNotFoundException, IOException
    {
        dbManager.updateStudentImage(student, imagePath);
    }

    /**
     * Updates the student's checkin.
     * @param student
     * @throws SQLException
     */
    public void updateCheckIn(Student student) throws SQLException
    {
        dbManager.updateCheckIn(student);
    }

    /**
     *Updates the student's checkout.
     * @param student
     * @throws SQLException
     */
    public void updateCheckOut(Student student) throws SQLException
    {
        dbManager.updateCheckOut(student);
    }

    /**
     *Gets all the lectures.
     * @return all lectures
     */
    public List<Lecture> getAllLectures()
    {
        return dbManager.getAllLectures();
    }

    /**
     *Add absence to the student
     * @param absence
     * @throws SQLException
     */
    public void addAbsence(Absence absence) throws SQLException
    {
        dbManager.addAbsence(absence);
    }

}
