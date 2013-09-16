package com.nazir.schedx.persist;

import android.app.AlarmManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.nazir.schedx.model.Assessment;
import com.nazir.schedx.model.Course;
import com.nazir.schedx.remainder.AlarmHelper;
import com.nazir.schedx.types.AssessmentTriggerMode;
import com.nazir.schedx.types.AssessmentType;
import com.nazir.schedx.util.Mapper;
import java.util.*;

import static com.nazir.schedx.persist.MySqliteOpenHelper.Assessment.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Tables.*;

public class AssessmentHelper
{
    private Assessment assessment;
    private List<Assessment> assessments;
    private String columns[] = {ID, COURSE_ID, DATE, TYPE, LOCATION, MEMO, TRIGGER_MODE};
    private Context context;
    int courseCodeIndx;
    int dateIndx;
    private SQLiteDatabase db;
    int idIndx;
    int locationIndx;
    int memoIndx;
    int triggerIndx;
    int typeIndx;
    
    public AssessmentHelper(Context context)
    {
    	this.context = context;
        db = MySqliteOpenHelper.getWritableDb(this.context);
        assessment = new Assessment();
        assessments = new ArrayList<Assessment>();
        
    }

    public long addAssessment(Assessment assessment1)
    {
        return db.insertOrThrow(ASSESSMENT, null, Mapper.mapToAssessment(assessment1));
    }

    

    public List<Assessment> getAllAssessments()
    {
        Cursor cursor = db.query(ASSESSMENT, columns, null, null, null, null, TYPE + " ASC");
       
        List<Assessment> list = new ArrayList<Assessment>();
        Assessment assessment1;
        CoursesHelper coursesHelper = new CoursesHelper(context);
        Course course;
        
        if(cursor.moveToFirst())
        {
            courseCodeIndx = cursor.getColumnIndex(COURSE_ID);
            dateIndx = cursor.getColumnIndex(DATE);
            typeIndx = cursor.getColumnIndex(TYPE);
            locationIndx = cursor.getColumnIndex(LOCATION);
            triggerIndx = cursor.getColumnIndex(TRIGGER_MODE);
            idIndx = cursor.getColumnIndex(ID);
            do
            {
                assessment1 = new Assessment();
                assessment1.setId(cursor.getInt(idIndx));
               
                course = coursesHelper.getCourse(cursor.getInt(courseCodeIndx));
                assessment1.setCourse(course);
                
                assessment1.setDate(cursor.getLong(dateIndx));
                assessment1.setLocation(cursor.getString(locationIndx));
                assessment1.setAssessmentType(AssessmentType.valueOf(cursor.getString(typeIndx)));
                
                String s = cursor.getString(triggerIndx);
                AssessmentTriggerMode assessmenttriggermode;
                if(s != null)
                    assessmenttriggermode = AssessmentTriggerMode.valueOf(s);
                else
                    assessmenttriggermode = null;
                assessment1.setTriggerMode(assessmenttriggermode);
                
                list.add(assessment1);
            } while(cursor.moveToNext());
        }
        return list;
    }

    public Assessment getAssessment(int id)
    {
        String whereArgs[] = {Integer.toString(id)};
        String whereClause = ID +" = ?";
        Cursor cursor = db.query(ASSESSMENT, columns, whereClause, whereArgs,
        		null, null, null);
       
        if(cursor.moveToFirst())
        {
            assessment.setId(id);
            String type = cursor.getString(cursor.getColumnIndex(TYPE));
            if(type != null)
            	assessment.setAssessmentType(AssessmentType.valueOf(type));
 
            assessment.setCourse(new CoursesHelper(context).getCourse(
            		cursor.getInt(cursor.getColumnIndex(COURSE_ID))));
            
            assessment.setDate(cursor.getLong(cursor.getColumnIndex(DATE)));
            assessment.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION)));
            assessment.setMemo(cursor.getString(cursor.getColumnIndex(MEMO)));
            
            String triggerMode = cursor.getString(cursor.getColumnIndex(TRIGGER_MODE));
            if(triggerMode != null)
            	assessment.setTriggerMode(AssessmentTriggerMode.valueOf(triggerMode));
           
        }
        return assessment;
    }

    public Cursor getAssessmentByType(AssessmentType assessmenttype)
    {
        if(assessmenttype.equals(AssessmentType.TYPE))
        {
            return getAssessments();
        } else
        {
            return db.query(ASSESSMENT, columns, "assessment_type = ?",
            		new String[]{assessmenttype.name()}, null, null, DATE+ " DESC");
        }
    }

    public Cursor getAssessments()
    {
        return db.query(ASSESSMENT, columns, null, null, null, null, DATE +" DESC");
    }

    public void insertMock(int num)
    {
        
    }

    public void terminate()
    {
        db.close();
    }

    public void update(Assessment assessment1)
    {
        db.update(ASSESSMENT, Mapper.mapToAssessment(assessment1),
        		"_id = ?", 
        		new String[]{Integer.toString(assessment1.getId())});
    }

	public void deleteByCourseId(int courseId) {
		String whereClause = COURSE_ID + " = ?";
		String whereArgs[] = {Integer.toString(courseId)};
		
		List<Assessment> assessments = getAssessmentsByCourse(courseId);
		AlarmHelper.cancelAssessmentAlarms(assessments, context);
		
		db.delete(ASSESSMENT, whereClause, whereArgs);		
	}

	private List<Assessment> getAssessmentsByCourse(int courseId) {
		
		String selection = COURSE_ID + " = ? ";
		String selectionArgs[] = {Integer.toString(courseId)};
		List<Assessment> assessments = new ArrayList<Assessment>();
		Assessment assessment;
		
		Cursor cursor = db.query(ASSESSMENT, new String[]{ID}, selection, selectionArgs, null, 
				null, null);
		
		if(cursor.moveToFirst()){
			do{
				assessment = new Assessment();
				assessment.setId(cursor.getInt(cursor.getColumnIndex(ID)));
				assessments.add(assessment);
			}while(cursor.moveToNext());
			
		}
		
		return assessments;
	}

	public void delete(int id)
    {
	
        db.delete(ASSESSMENT, ID+ " = ?", new String[]{Integer.toString(id)});
    }

    public void disconnect()
    {
        db.close();
    }
    
}
