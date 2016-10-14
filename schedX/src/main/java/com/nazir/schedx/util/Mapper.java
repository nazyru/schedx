package com.nazir.schedx.util;

import android.content.ContentValues;
import com.nazir.schedx.model.*;
import com.nazir.schedx.persist.MySqliteOpenHelper;
import com.nazir.schedx.types.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Lectures.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Courses.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.ClassRep.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Todos.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Assessment.*;

public class Mapper
{

    public static ContentValues mapToAssessment(Assessment assessment)
    {
        if(assessment == null)
            return null;
        
        ContentValues contentvalues = new ContentValues();
        
        contentvalues.put(MySqliteOpenHelper.Assessment.COURSE_ID, assessment.getCourse().getID());
        contentvalues.put(DATE, Long.valueOf(assessment.getDate()));
        contentvalues.put(LOCATION, assessment.getLocation());
        contentvalues.put(MEMO, assessment.getMemo());
        
        AssessmentTriggerMode assessmenttriggermode = assessment.getTriggerMode();
      
        if(assessmenttriggermode != null)
            contentvalues.put(TRIGGER_MODE, assessment.getTriggerMode().name());
        
        if(assessment.getAssessmentType() != null)
            contentvalues.put(TYPE, assessment.getAssessmentType().name());
        
        return contentvalues;
    }

    public static ContentValues mapToClassRep(ClassRep classrep)
    {
        if(classrep == null)
            return null;
      
            ContentValues contentvalues = new ContentValues();
            contentvalues.put(REG_NO, classrep.getRegNumber());
            contentvalues.put(MySqliteOpenHelper.ClassRep.NAME, classrep.getName());
            contentvalues.put(PHONE_NUMBER, classrep.getPhoneNumber());
            contentvalues.put(EMAIL_ADDRESS, classrep.getEmailAddress());
            
            return contentvalues;
    }

    public static ContentValues mapToCourse(Course course)
    {
        if(course == null)
        {
            return null;
        } else
        {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put(COURSE_CODE, course.getCourseCode());
            contentvalues.put(COURSE_TITLE, course.getCourseTitle());
            contentvalues.put(COURSE_UNIT, course.getCourseUnit());
            return contentvalues;
        }
    }

    public static ContentValues mapToLecture(Lecture lecture)
    {
        if(lecture == null)
        	return null;
            
        	ContentValues contentvalues = new ContentValues();
            contentvalues.put(MySqliteOpenHelper.Lectures.COURSE_ID, lecture.getCourse().getID());
            contentvalues.put(DAY, lecture.getDay());
            contentvalues.put(LECTURER, lecture.getLecturer());
            contentvalues.put(START_TIME, Long.valueOf(lecture.getStartTime()));
            contentvalues.put(END_TIME, Long.valueOf(lecture.getEndTime()));
            contentvalues.put(VENUE, lecture.getVenue());
            
            if(lecture.getStatus() != null)
                contentvalues.put(STATUS, lecture.getStatus().name());
            
            if(lecture.getClassRep() != null)
                contentvalues.put(CLASS_REP_ID, lecture.getClassRep().getId());
            
            if(lecture.getAlarmTrigger() != null)
            	contentvalues.put(LECTURE_TRIGGER, lecture.getAlarmTrigger().name());
    
        return contentvalues;
    }

    public static ContentValues mapTodos(Todo todo)
    {
        ContentValues contentvalues;
        if(todo == null)
        {
            contentvalues = null;
        } else
        {
            contentvalues = new ContentValues();
            contentvalues.put(MySqliteOpenHelper.Todos.NAME, todo.getName());
            contentvalues.put(DESCRIPTION, todo.getDescription());
            contentvalues.put(TIME, todo.getTime());
            contentvalues.put(RATING, todo.getRating());
            
            if(todo.getAlarmTrigger() != null)
                contentvalues.put(TRIGGER, todo.getAlarmTrigger().name());
            
            if(todo.getAlarmRepeatMode() != null)
                contentvalues.put(REPEAT_MODE, todo.getAlarmRepeatMode().name());
        }
        return contentvalues;
    }
}
