package attendence.be;

/**
 * The business entity class that represents the teacher.
 * @author Jacob Enemark
 */
public class Teacher extends Person
{
    /**
     * The teacher's default contructor.
     * @param id The id of the teacher.
     * @param firstName The teacher's first name.
     * @param lastName The teacher's last name.
     * @param username The teacher's username.
     * @param password The password matching the teacher's username.
     */
    public Teacher(int id, String firstName, String lastName, String email, String username, String password, String phoneNum)
    {
        super(id, firstName, lastName, email, username, password, phoneNum);
    }

}
