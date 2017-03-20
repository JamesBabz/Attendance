/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.model;

import attendance.be.Person;
import attendance.bll.SaveFileManager;
import attendance.dal.SaveFileDAO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author thomas
 */
public class LoginModel
{
    private SaveFileManager saveFileManager;
    private SaveFileDAO saveFileDAO;
    

    
    private static LoginModel instance;
        public static LoginModel getInstance()
    {
        if (instance == null)
        {
            instance = new LoginModel();
        }
        return instance;
    }

    private LoginModel()
    {
        saveFileManager = new SaveFileManager();
        saveFileDAO = new SaveFileDAO("LoginData.txt");

    }
    
        public String[] loadLoginData() throws IOException 
    {
       return saveFileManager.loadLoginData();

    }

    public void saveLoginData(String userName, String passWord) throws IOException 
    {
        saveFileManager.saveLoginData(userName, passWord);
        
    }
        
}
