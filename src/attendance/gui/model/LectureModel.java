/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.model;

import attendance.be.Lecture;
import attendance.bll.PersonManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class LectureModel
{
    
    private List<Lecture> lectures;
    
    private static LectureModel instance;
    private PersonManager personManager;

    public static LectureModel getInstance()
    {
        if (instance == null)
        {
            instance = new LectureModel();
        }
        return instance;
    }

    private LectureModel()
    {
        try
        {
            personManager = new PersonManager();
            lectures = personManager.getAllLectures();
        }
        catch (SQLException | IOException ex)
        {
            Logger.getLogger(LectureModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Lecture> getLectures()
    {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures)
    {
        this.lectures = lectures;
    }
}
