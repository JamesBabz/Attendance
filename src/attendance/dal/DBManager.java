package attendance.dal;

import attendance.be.Absence;
import attendance.be.Lecture;
import attendance.be.Person;
import attendance.be.Student;
import attendance.be.Teacher;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * This class handles all the Data from the Database once a connection has been
 * established.
 *
 * @author Stephan Fuhlendorff, Jacob Enemark, Simon Birkedal, Thomas Hansen
 */
public final class DBManager
{

    private ConnectionManager cm;

    private final List<Student> students;
    private final List<Teacher> teachers;
    private final List<Absence> absences;
    private final List<Lecture> lectures;

    /**
     * The default constructor for the database manager.
     *
     * @throws SQLException
     * @throws IOException
     */
    public DBManager() throws SQLException, IOException
    {
        this.lectures = new ArrayList<>();
        this.absences = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.students = new ArrayList<>();
        this.cm = new ConnectionManager();
        setAllPeople();
        setAllLectures();
    }

    /**
     * Sets the data for each person in the database.
     *
     * @throws SQLException
     * @throws IOException
     */
    public void setAllPeople() throws SQLException, IOException
    {
        String sql = "SELECT * FROM People";

        try (Connection con = cm.getConnection())
        {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                int id = rs.getInt("ID");
                String fName = rs.getString("FirstName");
                String lName = rs.getString("LastName");
                String email = rs.getString("Email");
                String user = rs.getString("UNILogin");
                String pass = rs.getString("Password");
                String phoneNum = rs.getString("PhoneNumber");
                if (rs.getBoolean("IsStudent"))
                {
                    Timestamp lastCheckin;
                    Timestamp lastCheckout;
                    String cName = rs.getString("Class");
                    if (rs.getDate("LastCheckedIn") != null)
                    {
                        lastCheckin = rs.getTimestamp("LastCheckedIn");
                    }
                    else
                    {
                        lastCheckin = null;
                    }
                    if (rs.getDate("LastCheckedOut") != null)
                    {
                        lastCheckout = rs.getTimestamp("LastCheckedOut");
                    }
                    else
                    {
                        lastCheckout = null;
                    }

                    byte[] imageBytes = rs.getBytes("ImageBinary");
                    BufferedImage studentImage = null;
                    if (imageBytes != null)
                    {
                        try
                        {
                            studentImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    Student student = new Student(id, fName, lName, email, user, pass, phoneNum, cName, lastCheckin, lastCheckout, studentImage);
                    students.add(student);
                }
                else
                {
                    Teacher teacher = new Teacher(id, fName, lName, email, user, pass, phoneNum);
                    teachers.add(teacher);
                }
            }
        }

    }

    /**
     * Gets all the students from the database.
     *
     * @return a list of all students.
     */
    public List<Student> getStudents()
    {
        return students;
    }

    /**
     * Gets all the teachers from the database.
     *
     * @return a list of all teachers.
     */
    public List<Teacher> getTeachers()
    {
        return teachers;
    }

    /**
     * Gets all people from the database.
     *
     * @return a list of all people. (Teachers and students)
     */
    public List<Person> getPeople()
    {
        ArrayList<Person> people = new ArrayList<>();

        people.addAll(getStudents());
        people.addAll(getTeachers());

        return people;
    }

    /**
     * Updates the database with a student image represented by binary data.
     *
     * @param student The student to update the image for.
     * @param image The image to be converted to binary data.
     * @throws IOException
     * @throws SQLException
     */
    public void updateStudentImage(Student student, String image) throws IOException, SQLException
    {
        String sql = "UPDATE People SET ImageBinary = ? WHERE ID = ?";
        int length;
        try (Connection con = cm.getConnection())
        {
            PreparedStatement ps = con.prepareStatement(sql);
            File imageFile = new File(image);
            FileInputStream fis = new FileInputStream(imageFile);
            length = (int) imageFile.length();
            byte[] imageBytes = new byte[length];
            int i = fis.read(imageBytes);
            ps.setBytes(1, imageBytes);
            ps.setInt(2, student.getId());
            ps.executeUpdate();

        }
    }

    /**
     * Updates the database with information about when a student last checked
     * in.
     *
     * @param student The student to update the check-in information about.
     * @throws SQLException
     */
    public void updateCheckIn(Student student) throws SQLException
    {
        String sql = "UPDATE People SET LastCheckedIn = ? WHERE ID = ?";

        try (Connection con = cm.getConnection())
        {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setTimestamp(1, (Timestamp) student.getLastCheckIn());
            ps.setInt(2, student.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Updates the database with information about when a student last checked
     * out.
     *
     * @param student The student to update the check-out information about.
     * @throws SQLException
     */
    public void updateCheckOut(Student student) throws SQLException
    {
        String sql = "UPDATE People SET LastCheckedOut = ? WHERE ID = ?";

        try (Connection con = cm.getConnection())
        {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setTimestamp(1, (Timestamp) student.getLastCheckOut());
            ps.setInt(2, student.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Get the absence of a student.
     * @param sID The id of the student.
     * @return a list of absence of the student specified by the id.
     * @throws SQLException 
     */
    public List<Absence> getSingleStudentAbsence(int sID) throws SQLException
    {
        absences.clear();
        String sql = "SELECT * FROM Absence WHERE StudentID = " + sID;

        try (Connection con = cm.getConnection())
        {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                int id = rs.getInt("ID");
                int studentID = rs.getInt("StudentID");
                int lectureID = rs.getInt("LectureID");
                Date date = rs.getDate("Date");
                Absence absence = new Absence(id, studentID, lectureID, date);
                absences.add(absence);
            }
        }
        return absences;
    }

    /**
     * Gets all student absence.
     * @param startDate The start date.
     * @param endDate The end date.
     * @return The summarized absence from start to end date.
     * @throws SQLServerException
     * @throws SQLException 
     */
    public List<Absence> getAllAbsence(LocalDate startDate, LocalDate endDate) throws SQLServerException, SQLException
    {
        java.sql.Date sDate = java.sql.Date.valueOf(startDate);
        java.sql.Date eDate = java.sql.Date.valueOf(endDate);
        String sql = "SELECT * FROM Absence WHERE Date >= '" + sDate + "' AND Date <= '" + eDate + "'";
        try (Connection con = cm.getConnection())
        {
            PreparedStatement ps = con.prepareStatement(sql);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {

                int id = rs.getInt("ID");
                int studentID = rs.getInt("StudentID");
                int lectureID = rs.getInt("LectureID");
                Date date = rs.getDate("Date");
                Absence absence = new Absence(id, studentID, lectureID, date);
                absences.add(absence);
            }
        }

        return absences;
    }

    /**
     * Sets the lectures for each teacher.
     * @throws SQLException 
     */
    public void setAllLectures() throws SQLException
    {
        String sql = "SELECT * FROM Lectures";
        try (Connection con = cm.getConnection())
        {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                int id = rs.getInt("ID");
                String lectureName = rs.getString("Lecture");
                String day = rs.getString("Day");
                int period = rs.getInt("Period");
                String className = rs.getString("ClassName");
                int semester = rs.getInt("Semester");
                int teacherId = rs.getInt("TeacherID");
                Lecture lecture = new Lecture(id, lectureName, day, period, className, semester, teacherId);
                lectures.add(lecture);
            }
        }
    }

    /**
     * Get the lectures for each teacher.
     * @return a list of lecture.
     */
    public List<Lecture> getAllLectures()
    {
        return lectures;
    }

    /**
     * Adds absence.
     * @param absence
     * @throws SQLException 
     */
    public void addAbsence(Absence absence) throws SQLException
    {
        String sql = "INSERT INTO Absence (StudentID, LectureID, Date) VALUES(?, ?, ?)";
        try (Connection con = cm.getConnection())
        {
            Statement st = con.createStatement();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, absence.getStudentId());
            ps.setInt(2, absence.getLectureId());
            java.sql.Date sqlDate = new java.sql.Date(absence.getDate().getTime());
            ps.setDate(3, sqlDate);

            ps.executeUpdate();
        }

    }
}
