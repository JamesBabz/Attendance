package attendence.gui.model;

import attendence.be.Student;
import attendence.be.Teacher;
import attendence.bll.SearchManager;
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
