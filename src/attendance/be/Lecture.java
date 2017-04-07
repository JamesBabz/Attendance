package attendance.be;

/**
 * The class holds the information about the lectures, the students have.
 *
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
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

    /**
     * The contructor for Lecture class.
     *
     * @param id the id of the lecture, the student has.
     * @param lectureName the name of the lecture, the student has.
     * @param day the day the student has the lecture
     * @param period the period the student has the lecture on the day
     * @param className the class that have the lecture
     * @param semester the semester that lecture belongs
     * @param teacherId the id of the teacher who has the lecture
     */
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

    /**
     * Gets the id of the lecture.
     *
     * @return the id of the lecture
     */
    public int getId()
    {
        return id;
    }

    /**
     * Gets the name of the lecture
     *
     * @return the name of the lecture
     */
    public String getLectureName()
    {
        return lectureName;
    }

    /**
     * Gets the day the student has the lecture
     *
     * @return the day
     */
    public String getDay()
    {
        return day;
    }

    /**
     * Gets the period the student has the lecture
     *
     * @return the period, which is int
     */
    public int getPeriod()
    {
        return period;
    }

    /**
     * Gets the name of the cass, which has the lecture
     *
     * @return String, the class name
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * Gets the semester, where the student has the lecture
     *
     * @return int, the semester
     */
    public int getSemester()
    {
        return semester;
    }

    /**
     * Gets the id of the tacher, who got that lecture.
     *
     * @return int, id of the tacher
     */
    public int getTeacherId()
    {
        return teacherId;
    }

    /**
     * Gets the start of the period the student has the lecture on the day
     *
     * @return int array, the start of period on the day
     */
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

    /**
     * Gets the end of the period the student has the lecture on the day
     *
     * @return int array, the end of period on the day
     */

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
