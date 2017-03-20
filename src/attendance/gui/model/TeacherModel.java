package attendance.gui.model;

import attendance.be.Student;
import attendance.be.Teacher;
import attendance.bll.SearchManager;
import java.util.List;

/**
 *
 * @author James
 */
public class TeacherModel
{

    private Teacher currentUser;
    SearchManager searchManager;

    private static TeacherModel instance;

    public static TeacherModel getInstance()
    {
        if (instance == null)
        {
            instance = new TeacherModel();
        }
        return instance;
    }

    private TeacherModel()
    {
          searchManager = new  SearchManager();

    }

    public Teacher getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(Teacher currentUser)
    {
        this.currentUser = currentUser;
    }
    
 
    public List search(List<Student> students, String searchQuery)
    {
       return  searchManager.search(students, searchQuery);
    }

}
