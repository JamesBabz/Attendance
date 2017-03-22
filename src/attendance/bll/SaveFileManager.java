/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.bll;

import attendance.dal.SaveFileDAO;
import java.io.IOException;

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
