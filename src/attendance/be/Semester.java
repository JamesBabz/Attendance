/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.be;

import java.util.Date;

/**
 *
 * @author James
 */
public class Semester
{

    int semesterNum;
    String className;
    Date startDate;
    Date endDate;

    /**
     * Use this to get the dates for the different semesters
     *
     * @param semesterNum The semester number to be used
     * @param className The full name of the class
     */
    public Semester(int semesterNum, String className)
    {
        this.semesterNum = semesterNum;
        this.className = className;
        setDates();
    }

    /**
     * Use this to get the current semester
     *
     * @param currentDate the current date (use new Date();)
     * @param className The class to be checked
     */
    public Semester(Date currentDate, String className)
    {
        this.className = className;
        setSemesterNum(currentDate);
        setDates();
    }

    private void setDates()
    {
        String yearString = className.substring(2, 6);
        int year = Integer.parseInt(yearString);

        switch (semesterNum)
        {
            case 1:
                startDate = new Date("8/22/" + year);
                endDate = new Date("12/31/" + year);
                break;
            case 2:
                year = year + 1;
                startDate = new Date("1/2/" + year);
                endDate = new Date("7/31/" + year);
                break;
            case 3:
                year = year + 1;
                startDate = new Date("8/22/" + year);
                endDate = new Date("12/31/" + year);
                break;
            case 4:
                year = year + 2;
                startDate = new Date("1/2/" + year);
                endDate = new Date("7/31/" + year);
                break;
            case 5:
                year = year + 2;
                startDate = new Date("8/22/" + year);
                endDate = new Date("12/31/" + year);
                break;
            default:
                break;
        }
    }

    public int getSemesterNum()
    {
        return semesterNum;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    private void setSemesterNum(Date currentDate)
    {
        String yearString = className.substring(2, 6);
        int year = Integer.parseInt(yearString);

        if (currentDate.before(new Date("12/31/" + year)))
        {
            semesterNum = 1;
        }
        else if (currentDate.before(new Date("7/31/" + year + 1)))
        {
            semesterNum = 2;
        }
        else if (currentDate.before(new Date("12/31/" + year + 1)))
        {
            semesterNum = 3;
        }
        else if (currentDate.before(new Date("7/31/" + year + 2)))
        {
            semesterNum = 4;
        }
        else if (currentDate.before(new Date("12/31/" + year + 2)))
        {
            semesterNum = 5;
        }
    }

}
