/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendence.be;

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

    public Semester(int semesterNum, String className)
    {
        this.semesterNum = semesterNum;
        this.className = className;
        setDates();
    }

    private void setDates()
    {
        String yearString = className.substring(2, 6);
        int year = 0;
        try
        {
            year = Integer.parseInt(yearString);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Something went wrong: " + e);
        }
        switch (semesterNum)
        {
            case 1:
                startDate = new Date("8/22/" + year);
                endDate = new Date("12/31/" + year);
                break;
            case 2:
                startDate = new Date("1/2/" + year + 1);
                endDate = new Date("7/31/" + year + 1);
                break;
            case 3:
                startDate = new Date("8/22/" + year + 1);
                endDate = new Date("12/31/" + year + 1);
                break;
            case 4:
                startDate = new Date("1/2/" + year + 2);
                endDate = new Date("7/31/" + year + 2);
                break;
            case 5:
                startDate = new Date("8/22/" + year + 2);
                endDate = new Date("12/31/" + year + 2);
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

}
