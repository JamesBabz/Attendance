package attendance.be;

import java.util.Date;

/**
 * This class holds all the information about a students absence.
 *
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public class Absence
{

    private int id;
    private Date date;
    private int studentId;
    private int lectureId;

    /**
     * The default constructor for the absence class.
     *
     * @param id The id of the students absence.
     * @param date The date of which the students absence occured.
     * @param lectureId The id for the lecture, the is student is absent.
     * @param studentId The id of the student.
     */
    public Absence(int id, int studentId, int lectureId, Date date)
    {
        this.id = id;
        this.studentId = studentId;
        this.lectureId = lectureId;
        this.date = date;
    }
public Absence(int studentId, int lectureId, Date date)
    {
        this.studentId = studentId;
        this.lectureId = lectureId;
        this.date = date;
    }

    /**
     * Gets the absence id.
     *
     * @return Returns the absence id.
     */
    public int getId()
    {
        return id;
    }

    /**
     * Sets the absence id.
     *
     * @param id The absence id to be set.
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * The date the absence occured.
     *
     * @return The date the absence occured.
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Sets a date on which the absence occured.
     *
     * @param date The date to set the absence for.
     */
    public void setDate(Date date)
    {
        this.date = date;
    }

    /**
     * Gets the student id of the person being away.
     *
     * @return Returns the id of the absenct person.
     */
    public int getStudentId()
    {
        return studentId;
    }

    /**
     * Sets a student id for the absent person.
     *
     * @param studentId The id to be set to being absent.
     */
    public void setStudentId(int studentId)
    {
        this.studentId = studentId;
    }

    /**
     * Sets id for the lecture, the student is absenct in. 
     * @param lectureId  The id for the lecture, the is student is absent.
     */
    public void setLectureId(int lectureId)
    {
        this.lectureId = lectureId;
    }

    /** 
     * Gets the id for the lecture.
     * @return the id of the lecture, the student is absence. 
     */
    public int getLectureId()
    {
        return lectureId;
    }
    
    

}
