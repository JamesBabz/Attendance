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

    public int[] getPeriodStart()
    {
        int[] returnArray = new int[2];

        switch (period)
        {
            case 1:
                returnArray[0] = 8;
                returnArray[1] = 15;
                break;
            case 2:
                returnArray[0] = 9;
                returnArray[1] = 00;
                break;
            case 3:
                returnArray[0] = 10;
                returnArray[1] = 00;
                break;
            case 4:
                returnArray[0] = 10;
                returnArray[1] = 45;
                break;
            case 5:
                returnArray[0] = 12;
                returnArray[1] = 00;
                break;
            case 6:
                returnArray[0] = 12;
                returnArray[1] = 45;
                break;
            case 7:
                returnArray[0] = 13;
                returnArray[1] = 45;
                break;
            case 8:
                returnArray[0] = 14;
                returnArray[1] = 30;
                break;
            default:
                returnArray = null;
                break;
        }

        return returnArray;
    }

    public int[] getPeriodEnd()
    {
        int[] returnArray = new int[2];

        switch (period)
        {
            case 1:
                returnArray[0] = 9;
                returnArray[1] = 00;
                break;
            case 2:
                returnArray[0] = 9;
                returnArray[1] = 45;
                break;
            case 3:
                returnArray[0] = 10;
                returnArray[1] = 45;
                break;
            case 4:
                returnArray[0] = 11;
                returnArray[1] = 30;
                break;
            case 5:
                returnArray[0] = 12;
                returnArray[1] = 45;
                break;
            case 6:
                returnArray[0] = 13;
                returnArray[1] = 30;
                break;
            case 7:
                returnArray[0] = 14;
                returnArray[1] = 30;
                break;
            case 8:
                returnArray[0] = 15;
                returnArray[1] = 15;
                break;
            default:
                returnArray = null;
                break;
        }

        return returnArray;
    }
}
