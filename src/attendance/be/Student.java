package attendance.be;

import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.ImageView;

/**
 *  This business entity class holds the information about the student and extends Person
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public class Student extends Person
{

    private String className;
    private int totalAbsence;
    private Timestamp lastCheckIn;
    private Timestamp lastCheckOut;
    private final String fullName;
    private BooleanProperty registered;
    private BufferedImage studentImage;
    private double percentageAbsence = 0;
    private ImageView profilePic;

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
     * @param studentImage the image of the student
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
        } else
        {
            this.registered = new SimpleBooleanProperty(false);
        }
    }

    /**
     * @return if the student is registered or not.
     */
    public BooleanProperty registeredProperty()
    {
        return registered;
    }

    /**
     * @return true if the student is registered.
     */
    public boolean isRegistered()
    {
        return this.registered.get();
    }

    /**
     * Sets that the student is registered.
     *
     * @param value boolean
     */
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

    /**
     * Gets the last checkin.
     *
     * @return timestamp
     */
    public Timestamp getLastCheckIn()
    {
        return lastCheckIn;
    }

    /**
     * Sets the last checkin.
     *
     * @param lastCheckIn
     */
    public void setLastCheckIn(Timestamp lastCheckIn)
    {
        this.lastCheckIn = lastCheckIn;
    }

    /**
     * Gets last checkout.
     *
     * @return
     */
    public Timestamp getLastCheckOut()
    {
        return lastCheckOut;
    }

    /**
     * Sets last checkout.
     *
     * @param lastCheckOut
     */
    public void setLastCheckOut(Timestamp lastCheckOut)
    {
        this.lastCheckOut = lastCheckOut;
    }

    /**
     * Gets the student's fullname.
     *
     * @return String
     */
    public String getFullName()
    {
        return fullName;
    }

    public double getPercentageAbsence()
    {
        return percentageAbsence;
    }

    public void setPercentageAbsence(LocalDate from, LocalDate to, List<Absence> absence)
    {
        int x = absence.size() + 1;

        setTotalAbsence(x);
        long diff = DAYS.between(from, to);
        diff = diff / 7 * 5 * 5;
        this.percentageAbsence = diff / x;
    }

    /**
     * Gets the image of the student.
     *
     * @return image
     */
    public ImageView getProfilePic()
    {
        return profilePic;
    }

    /**
     * Sets the image of the student.
     *
     * @param profilePic
     */
    public void setProfilePic(ImageView profilePic)
    {
        this.profilePic = profilePic;
    }

}
