/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendence.bll;

import attendence.be.Person;
import attendence.dal.SaveFileDAO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thomas
 */
public class SaveFileManager
{
    private SaveFileDAO saveFileDAO;

    public SaveFileManager()
    {
        saveFileDAO = new SaveFileDAO("LoginData.txt");
    }
    
    
    public void saveLoginData(String userName, String passWord) throws IOException 
    {
        
            saveFileDAO.saveLogin(userName, passWord);
  
    }
    
    public String[] loadLoginData() throws IOException 
    {
        
            return saveFileDAO.loadLogin();
 
    }

    
}
