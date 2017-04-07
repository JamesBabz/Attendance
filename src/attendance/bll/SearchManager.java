package attendance.bll;

import attendance.be.Student;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the search.
 *
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public class SearchManager
{

    /**
     * Searches through a list of students, then returns a new ArrayList of
     * students that contains the searchquery requirement.
     * @param students the list of students to search in.
     * @param searchQuery The string to match the songs against.
     * @return Returns a new ArrayList of songs that matches the searchquery.
     */
    public ArrayList<Student> search(List<Student> students, String searchQuery)
    {
        ArrayList<Student> result = new ArrayList<>();

        for (Student student : students)
        {
            String fullName = student.getFullName().trim().toLowerCase();

            if (fullName.contains(searchQuery.toLowerCase().trim())
                    && !result.contains(student))
            {
                result.add(student);
            }
        }

        return result;
    }
}
