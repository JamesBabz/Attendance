/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.model;

import attendance.be.Lecture;
import java.util.List;

/**
 *
 * @author James
 */
public class LectureModel
{
    
    private List<Lecture> lectures;
    
    private static LectureModel instance;

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
