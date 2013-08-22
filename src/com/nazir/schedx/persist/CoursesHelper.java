package com.nazir.schedx.persist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.nazir.schedx.model.Course;
import com.nazir.schedx.util.Mapper;
import java.util.ArrayList;
import java.util.List;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Courses.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Tables.*;

public class CoursesHelper
{
    private Context context;
    private SQLiteDatabase db;
    private static String[] cols = {_ID, COURSE_CODE, COURSE_TITLE, COURSE_UNIT};
    
    public CoursesHelper(Context context1)
    {
        context = context1;
        db = MySqliteOpenHelper.getWritableDb(context);
    }

    public long addCourse(Course course)
    {
        return db.insertOrThrow(COURSES, null, Mapper.mapToCourse(course));
    }

    public void delete(int id)
    {
    	//We have to first delete all the lectures associated with the course
    	//and assessments as well
    	
        LecturesHelper lectureHelper = new LecturesHelper(context);
        lectureHelper.deleteByCourseId(id);
        lectureHelper.disconnect();
        
        AssessmentHelper assmentHelper = new AssessmentHelper(context);
        assmentHelper.deleteByCourseId(id);
        assmentHelper.disconnect();
        
        String whereClause = _ID + "= ?";
        String whereArgs[] = {Integer.toString(id)};
        db.delete(COURSES, whereClause, whereArgs);
    }

    public void disconnect()
    {
        db.close();
    }

    public List<Course> getAllCourses()
    {
        ArrayList<Course> arraylist = new ArrayList<Course>();
        Course course = new Course();
        Cursor cursor = db.query(COURSES, cols, null, null, null, null, null);
        if(cursor.moveToFirst())
        {
            int i = cursor.getColumnIndex("course_code");
            int j = cursor.getColumnIndex("course_title");
            int k = cursor.getColumnIndex("course_unit");
            do
            {
                course.setCourseCode(cursor.getString(i));
                course.setCourseTitle(cursor.getString(j));
                course.setCourseUnit(cursor.getInt(k));
                arraylist.add(course);
            } while(cursor.moveToNext());
        }
        return arraylist;
    }

    public Cursor getCourses()
    {
        return db.query(COURSES, new String[] {_ID, COURSE_CODE, COURSE_TITLE, COURSE_UNIT},
        		null, null, null, null, null);
    }

    public void insertMockData(int i)
    {
        db.delete("courses", null, null);
        int j = 0;
        do
        {
            if(j >= i)
                return;
            addCourse(new Course((new StringBuilder("Course Code ")).append(j).toString(), (new StringBuilder("Course Title ")).append(j).toString()));
            j++;
        } while(true);
    }

    public void terminate()
    {
        db.close();
    }

    public void updateCourse(Course course)
    {
    	String whereClause = _ID + " = ?";
        String whereArgs[] = {Integer.toString(course.getID())};
        
        db.update(COURSES, Mapper.mapToCourse(course), whereClause, whereArgs);
    }

	public Course getCourse(int courseId) {
		
		String selection = _ID + " = ?";
		String selectionArgs[] = {Integer.toString(courseId)};
		
		Cursor cursor = db.query(COURSES, cols, selection, selectionArgs, null, null, null);
		
		if(cursor.moveToFirst()){
			Course course = new Course();
			course.setID(courseId);
			course.setCourseCode(cursor.getString(cursor.getColumnIndex(COURSE_CODE)));
			course.setCourseTitle(cursor.getString(cursor.getColumnIndex(COURSE_TITLE)));
			course.setCourseUnit(cursor.getInt(cursor.getColumnIndex(COURSE_UNIT)));
			
			return course;
		}
		
		return null;
	}

   
}
