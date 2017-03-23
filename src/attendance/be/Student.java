package attendance.be;

import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;

/**
 *
 * @author Jacob Enemark
 */
public class Student extends Person
{

    private String className;
    private int totalAbsence;
    private Timestamp lastCheckIn;
    private Timestamp lastCheckOut;
    private String fullName;
    private BooleanProperty registered;
    private BufferedImage studentImage;
    
    /**
     * The default constructor for the student class.
     *
     * @param id The id of the student.
     * @param firstName The student's first name.
     * @param lastName The student's last name.
     * @param email
     * @param username The student's user name.
     * @param password The password matching the student's user name.
     * @param phoneNum
     * @param className The student's class name, that is the name of the class
     * @param lastCheckIn in which the student participates. e.g. A
     * @param lastCheckout
     * @param studentImage
     */
    public Student(int id, String firstName, String lastName, String email, String username, String password, String phoneNum, String className, Timestamp lastCheckIn, Timestamp lastCheckout, BufferedImage studentImage)
    {
        super(id, firstName, lastName, email, username, password, phoneNum);
        this.className = className;
        this.lastCheckIn = lastCheckIn;
        this.lastCheckOut = lastCheckout;
        this.fullName = firstName + " " + lastName;
        setInitRegister(lastCheckIn, lastCheckout);
        this.studentImage = studentImage;
    } 


    /**
     * Get the value of studentImage
     *
     * @return the value of studentImage
     */
    public BufferedImage getStudentImage()
    {
        return studentImage;
    }

    /**
     * Set the value of studentImage
     *
     * @param studentImage new value of studentImage
     */
    public void setStudentImage(BufferedImage studentImage)
    {
        this.studentImage = studentImage;
    }

    private void setInitRegister(Timestamp lastCheckIn1, Timestamp lastCheckout)
    {
        if (lastCheckIn1 != null && lastCheckout != null)
        {
            this.registered = new SimpleBooleanProperty(lastCheckIn1.after(lastCheckOut));
        }
        else
        {
            this.registered = new SimpleBooleanProperty(false);
        }
    }

    public BooleanProperty registeredProperty()
    {
        return registered;
    }

    public boolean isRegistered()
    {
        return this.registered.get();
    }

    public void setRegistered(boolean value)
    {
        this.registered.set(value);
    }

    /**
     * Gets the class name of the student.
     *
     * @return
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * Sets the class name of the student.
     *
     * @param className The class name.
     */
    public void setClassName(String className)
    {
        this.className = className;
    }

    /**
     * Sets the student's total absence.
     *
     * @param totalAbsence The total absence.
     */
    public void setTotalAbsence(int totalAbsence)
    {
        this.totalAbsence = totalAbsence;
    }

    /**
     * Gets the student's total absence.
     *
     * @return Returns the student's total absence represented by an integer
     * value.
     */
    public int getTotalAbsence()
    {
        return totalAbsence;
    }

    public Timestamp getLastCheckIn()
    {
        return lastCheckIn;
    }

    public void setLastCheckIn(Timestamp lastCheckIn)
    {
        this.lastCheckIn = lastCheckIn;
    }

    public Timestamp getLastCheckOut()
    {
        return lastCheckOut;
    }

    public void setLastCheckOut(Timestamp lastCheckOut)
    {
        this.lastCheckOut = lastCheckOut;
    }

    public String getFullName()
    {
        return fullName;
    }

}
