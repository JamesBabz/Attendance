/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.bll;

import attendance.dal.SaveFileDAO;
import java.io.IOException;

/**
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public class SaveFileManager
{
    private SaveFileDAO saveFileDAO;

/**
 * Contructor.
 */
    public SaveFileManager()
    {
        saveFileDAO = new SaveFileDAO("LoginData.txt");
    }
    
/**
 * Sends the login data further to SaveFileDAO.
 * @param userName String to be saved
 * @param passWord String to be saved
 * @throws IOException 
 */
    public void saveLoginData(String userName, String passWord) throws IOException 
    {
        
            saveFileDAO.saveLogin(userName, passWord);
  
    }
    /**
     * Loads the login data from SaveFiledAO
     * @return String list with login data
     * @throws IOException 
     */
    public String[] loadLoginData() throws IOException 
    {
        
            return saveFileDAO.loadLogin();
 
    }

    
}
