package attendance.be;

import java.io.Serializable;

/** 
 * The person business entity class. This class holds information about the student.
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public abstract class Person implements Serializable
{

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String phoneNum;

    /**
     * The default constructor for the person class.
     * @param id The id of the person.
     * @param firstName The first name of the person.
     * @param lastName The last name of the person.
     * @param username The user name of the person.
     * @param password The password to match the username.
     */
    public Person(int id, String firstName, String lastName, String email, String username, String password, String phoneNum)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNum = phoneNum;
    }

    /**
     * Gets the id of the person.
     * @return Returns the id of the person.
     */
    public int getId()
    {
        return id;
    }

    /**
     * Sets a new id for the person.
     * @param id The new id to be set.
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Gets the first name of the person.
     * @return Returns the person's first name.
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Sets the person's first name.
     * @param firstName The new first name to give the person.
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the person.
     * @return Returns the person's last name.
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Sets the person's last name.
     * @param lastName The last name to be set.
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Gets the person's username.
     * @return Returns the username of the person.
     */
    public String getUserName()
    {
        return username;
    }

    /**
     * Sets the person's username.
     * @param username The new username to assign to a person.
     */
    public void setUserName(String username)
    {
        this.username = username;
    }

    /**
     * Gets the password of the matching user.
     * @return Returns the matching password for the person.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets a new password for a specified person.
     * @param password The new password to be assigned to the person's username.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
}
