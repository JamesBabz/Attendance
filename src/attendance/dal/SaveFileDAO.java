/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.dal;

import attendance.be.Person;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author thomas
 */
public class SaveFileDAO
{



    public SaveFileDAO(String fileName)
    {

    }

    public void saveLogin(String userName, String passWord) throws IOException
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("LoginData.txt")))
        {
            
            bw.append(userName 
                    + ", " + passWord);
         
        }
    }

    public String[] loadLogin() throws FileNotFoundException, IOException
    {
//        ArrayList<String> userData = new ArrayList<>();
        String[] loadedArray = new String[2];

       try (BufferedReader br = new BufferedReader(new FileReader("LoginData.txt")))
        {
            Scanner scanner = new Scanner(br);
            while (scanner.hasNext())
            {
                     String line = scanner.nextLine();
                String[] fields = line.split(",");
                loadedArray[0] = fields[0].trim();
                loadedArray[1] = fields[1].trim();
            
        
            }
        }
        return loadedArray;
    }
}
