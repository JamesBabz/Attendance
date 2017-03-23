package attendance.dal;

import attendance.be.Absence;
import attendance.be.Person;
import attendance.be.Student;
import attendance.be.Teacher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jacob Enemark
 */
public class PersonDAO {

    private static PersonDAO instance;

    public static PersonDAO getInstance()
    {
        if (instance == null)
        {
            instance = new PersonDAO();
        }
        return instance;
    }

    private PersonDAO()
    {

    }

    public List<Student> getAllStudents()
    {
        List<Student> returnList = new ArrayList<>();

//        try (BufferedReader CSVFile = new BufferedReader(
//                new FileReader("Students.csv")))
//        {
//            CSVFile.readLine(); // Skip first line (header)
//            String line = CSVFile.readLine();
//            while (line != null)
//            {
//                Random r = new Random();
//                char c = (char) (r.nextInt(26) + 'a');
//                int num = r.nextInt(4);
//                String[] dataArray = line.split(",");
//                returnList.add(new Student(
//                        Integer.parseInt(dataArray[0]),
//                        dataArray[1].trim(),
//                        dataArray[2].trim(),
//                        dataArray[3].trim(),
//                        dataArray[4].trim(),
//                        c + num + ""));
//                line = CSVFile.readLine();
//            }
//
//        }
//        catch (IOException ex)
//        {
//            System.out.println(ex);
//            return null;
//        }
//         returnList.add(new Patient(1, "ew","ewqeqw","ewq"));
        return returnList;
    }

    public List<Teacher> getAllTeachers()
    {
        List<Teacher> returnList = new ArrayList<>();

//        try (BufferedReader CSVFile = new BufferedReader(
//                new FileReader("Teachers.csv")))
//        {
//            CSVFile.readLine(); // Skip first line (header)
//            String line = CSVFile.readLine();
//            while (line != null)
//            {
//                String[] dataArray = line.split(",");
//                returnList.add(new Teacher(
//                        Integer.parseInt(dataArray[0]),
//                        dataArray[1].trim(),
//                        dataArray[2].trim(),
//                        dataArray[3].trim(),
//                        dataArray[4].trim()));
//                line = CSVFile.readLine();
//            }
//
//        }
//        catch (IOException ex)
//        {
//            System.out.println(ex);
//            return null;
//        }
        return returnList;
    }

    public List<Person> getAllPeople()
    {
        ArrayList<Person> people = new ArrayList<>();
        
        people.addAll(getAllStudents());
        people.addAll(getAllTeachers());
        
        return people;
    }

}
