/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.dal;

import attendance.be.Absence;
import attendance.be.Person;
import attendance.be.Student;
import attendance.be.Teacher;
import attendance.bll.DateTimeManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author James
 */
public class DBManager
{

    ConnectionManager cm;
    DateTimeManager DTMan;

    List<Student> students = new ArrayList<>();
    List<Teacher> teachers = new ArrayList<>();
    List<Absence> absences = new ArrayList<>();

    public DBManager() throws SQLException, IOException
    {
        this.cm = new ConnectionManager();
        setAllPeople();
    }

    public void setAllPeople() throws SQLException
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
                    Student student = new Student(id, fName, lName, email, user, pass, phoneNum, cName, lastCheckin, lastCheckout);
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

    public List<Absence> getAbsence(int sID) throws SQLException
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

}
