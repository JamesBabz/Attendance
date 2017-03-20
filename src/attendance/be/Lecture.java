/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.be;

/**
 *
 * @author James
 */
public class Lecture
{

    int id;
    String lectureName;
    String day;
    int period;
    String className;
    int semester;
    int teacherId;

    public Lecture(int id, String lectureName, String day, int period, String className, int semester, int teacherId)
    {
        this.id = id;
        this.lectureName = lectureName;
        this.day = day;
        this.period = period;
        this.className = className;
        this.semester = semester;
        this.teacherId = teacherId;
    }

    public int getId()
    {
        return id;
    }

    public String getLectureName()
    {
        return lectureName;
    }

    public String getDay()
    {
        return day;
    }

    public int getPeriod()
    {
        return period;
    }

    public String getClassName()
    {
        return className;
    }

    public int getSemester()
    {
        return semester;
    }

    public int getTeacherId()
    {
        return teacherId;
    }
}
