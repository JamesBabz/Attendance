/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendence.bll;

import attendence.be.Student;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thomas
 */
public class SearchManager
{
      public ArrayList<Student> search(List<Student> students, String searchQuery)
    {        
        ArrayList<Student> result = new ArrayList<>();
        
        for (Student student : students)
        {
            String fullName= student.getFullName().trim().toLowerCase();
            
            if (fullName.contains(searchQuery.toLowerCase().trim())
                    && !result.contains(student))
            {
                result.add(student);
            }
        }
        
        return result;
    }
}
