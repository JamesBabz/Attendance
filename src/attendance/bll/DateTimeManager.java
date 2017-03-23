package attendance.bll;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

/**
 * This class manages anything related to date and time.
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public class DateTimeManager
{
    private final ArrayList<String> capitalizedMonths;
    
    /**
     * The default constructor for the datetimemanager class.
     */
    public DateTimeManager()
    {
        this.capitalizedMonths = new ArrayList<>();
    }
    
    /**
     * Gets the month we are currently in.
     * @return Returns the current month represented by a String value.
     */
    public String getCurrentMonth()
    {
        String monthOfYear = LocalDateTime.now().toLocalDate().getMonth().name().toLowerCase();
        String monthCapitalized = capitalize(monthOfYear);
        return monthCapitalized;
    }
    
    /**
     * Gets the current day in this month.
     * @return Returns the current day in this month represented by an integer value.
     */
    public int getCurrentDayOfMonth()
    {
        int dayOfMonth = LocalDateTime.now().toLocalDate().getDayOfMonth();
        return dayOfMonth;
    }
    
    /**
     * Gets all month in a year, then converts the first letter to uppercase and
     * returns a new String arraylist representing each month.
     * @return Returns all months in a year as capitalized month names.
     */
    public ArrayList<String> getMonthsCapitalized()
    {        
        for (Month month : Month.values())
        {
            String monthName = month.name().toLowerCase();
            String upperMonth = this.capitalize(monthName);
            this.capitalizedMonths.add(upperMonth);
        }
        
        return this.capitalizedMonths;
    }
    
    /**
     * Converts the first character in a literal String to upper case.
     *
     * @param line The string to be converted.
     * @return Returns a new String with the first character in the sequence as
     * capitalized. (Upper case)
     */
    private String capitalize(String line)
    {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
