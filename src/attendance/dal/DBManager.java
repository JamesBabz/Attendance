package attendance.dal;

import attendance.be.Absence;
import attendance.be.Lecture;
import attendance.be.Person;
import attendance.be.Student;
import attendance.be.Teacher;
import attendance.bll.DateTimeManager;
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
 *
 * @author James
 */
public final class DBManager
{

    ConnectionManager cm;
    DateTimeManager DTMan;

    List<Student> students = new ArrayList<>();
    List<Teacher> teachers = new ArrayList<>();
    List<Absence> absences = new ArrayList<>();
    List<Lecture> lectures = new ArrayList<>();

    public DBManager() throws SQLException, IOException
    {
        this.cm = new ConnectionManager();
        setAllPeople();
        setAllLectures();
    }

    public void setAllPeople() throws SQLException, IOException
    {
        String sql = "SELECT * FROM People";

        try (Connection con = cm.getConnection())
        {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                int id = rs.getInt(1);
                String fName = rs.getString(2);
                String lName = rs.getString(3);
                String email = rs.getString(4);
                String user = rs.getString(5);
                String pass = rs.getString(6);
                String phoneNum = rs.getString(7);
                if (rs.getBoolean("IsStudent"))
                {
                    Timestamp lastCheckin;
                    Timestamp lastCheckout;
                    String cName = rs.getString(9);
                    if (rs.getDate(10) != null)
                    {
                        lastCheckin = rs.getTimestamp(10);
                    }
                    else
                    {
                        lastCheckin = null;
                    }
                    if (rs.getDate(11) != null)
                    {
                        lastCheckout = rs.getTimestamp(11);
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

    public List<Student> getStudents()
    {
        return students;
    }

    public List<Teacher> getTeachers()
    {
        return teachers;
    }

    public List<Person> getPeople()
    {
        ArrayList<Person> people = new ArrayList<>();

        people.addAll(getStudents());
        people.addAll(getTeachers());

        return people;
    }

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
                int id = rs.getInt(1);
                int studentID = rs.getInt(2);
                int lectureID = rs.getInt(3);
                Date date = rs.getDate(4);
                Absence absence = new Absence(id, studentID, lectureID, date);
                absences.add(absence);
            }
        }
        return absences;
    }

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

                int id = rs.getInt(1);
                int studentID = rs.getInt(2);
                int lectureID = rs.getInt(3);
                Date date = rs.getDate(4);
                Absence absence = new Absence(id, studentID, lectureID, date);
                absences.add(absence);
            }
        }

        return absences;
    }

    public void setAllLectures() throws SQLException
    {
        String sql = "SELECT * FROM Lectures";
        try (Connection con = cm.getConnection())
        {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                int id = rs.getInt(1);
                String lectureName = rs.getString(2);
                String day = rs.getString(3);
                int period = rs.getInt(4);
                String className = rs.getString(5);
                int semester = rs.getInt(6);
                int teacherId = rs.getInt(7);
                Lecture lecture = new Lecture(id, lectureName, day, period, className, semester, teacherId);
                lectures.add(lecture);
            }
        }
    }

    public List<Lecture> getAllLectures()
    {
        return lectures;
    }

    public void addAbsence(Absence absence) throws SQLException
    {
//        String sql = "INSERT INTO Absence VALUES(?, ?, ?, ?)";
//        int id = 0;
//            Random rand = new Random();
//            int shit = rand.nextInt(40);
//            System.out.println(shit);
//        try (Connection con = cm.getConnection())
//        {
//            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setInt(1, shit);
//            ps.setInt(2, absence.getStudentId());
//            ps.setInt(3, absence.getLectureId());
//            java.sql.Date sqlDate = new java.sql.Date(absence.getDate().getTime());
//            ps.setDate(4, sqlDate);
//            ps.executeQuery();
//        }
    }
}
