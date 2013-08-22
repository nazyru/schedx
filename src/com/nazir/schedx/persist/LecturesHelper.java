package com.nazir.schedx.persist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.nazir.schedx.model.ClassRep;
import com.nazir.schedx.model.Course;
import com.nazir.schedx.model.Lecture;
import com.nazir.schedx.types.Day;
import com.nazir.schedx.types.LectureAlarmTrigger;
import com.nazir.schedx.types.Status;
import com.nazir.schedx.util.DateTimeHelper;
import com.nazir.schedx.util.Mapper;
import java.util.ArrayList;
import java.util.List;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Lectures.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.ClassRep.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Tables.*;


public class LecturesHelper
{
    private Context context;
    private SQLiteDatabase db;
    private String cols[] = {MySqliteOpenHelper.Lectures._ID, COURSE_ID, START_TIME, DAY, END_TIME, 
    		VENUE, CLASS_REP_ID, STATUS, LECTURER, LECTURE_TRIGGER};

    public LecturesHelper(Context context)
    {
        this.context = context;
        db = MySqliteOpenHelper.getWritableDb(context);
    }

    public long addLecture(Lecture lecture)
    {
    	lecture.setStatus(Status.ONGOING);
        return db.insertOrThrow(LECTURES, null, Mapper.mapToLecture(lecture));
    }

    public void addMockLecture(int i)
    {
        
    }

    public void addMockLectures(int record)
    {
       
    }

    public boolean assignClassRep(int classRepId, int lectureId)
    {
    		ClassRep classRep = new ClassRepHelper(context).getClassRep(classRepId);
    		
            String selection = "_id = ? ";
            String selectionArgs[] = {Integer.toString(lectureId)};
            
            Cursor lectureCursor = db.query(LECTURES, cols, selection, selectionArgs, null, null, null);
            
            if(lectureCursor.moveToFirst())
            {
                Lecture lecture = new Lecture();
                lecture.setClassRep(classRep);
                
                int courseId = lectureCursor.getInt(lectureCursor.getColumnIndex(COURSE_ID));
                Course course = new CoursesHelper(context).getCourse(courseId);
                lecture.setCourse(course);
                
                lecture.setStartTime(lectureCursor.getLong(lectureCursor.getColumnIndex(START_TIME)));
                lecture.setId(lectureId);
                lecture.setDay(lectureCursor.getString(lectureCursor.getColumnIndex(DAY)));
                lecture.setEndTime(lectureCursor.getLong(lectureCursor.getColumnIndex(END_TIME)));
                lecture.setVenue(lectureCursor.getString(lectureCursor.getColumnIndex(VENUE)));
                lecture.setLecturer(lectureCursor.getString(lectureCursor.getColumnIndex(LECTURER)));
                
                String trigger = lectureCursor.getString(lectureCursor.getColumnIndex(LECTURE_TRIGGER));
                lecture.setAlarmTrigger( trigger != null ? LectureAlarmTrigger.valueOf(trigger) : null);
                
                String status = lectureCursor.getString(lectureCursor.getColumnIndex(STATUS));
                lecture.setStatus(status != null ? Status.valueOf(status) : null);
                
                update(lecture);
                
                return true;
            }
            return false;
    }

    public void delete(int id)
    {
        String whereClause = MySqliteOpenHelper.Lectures._ID + " = ?";
        String whereArgs[] = {Integer.toString(id)};
        
        db.delete(LECTURES, whereClause, whereArgs);
    }

    public void disconnect()
    {
        db.close();
    }

    public Lecture getLectureSchedlule(int id)
    {
        Lecture lecture = new Lecture();
        String selection = MySqliteOpenHelper.Lectures._ID + " = ? ";
        String selectionArgs[] = {Integer.toString(id)};
       
        Cursor cursor = db.query(LECTURES, cols, selection, selectionArgs, null, null, null);
        
        if(cursor.moveToFirst())
        {
            lecture.setDay(cursor.getString(cursor.getColumnIndex(DAY)));
            lecture.setStartTime(cursor.getLong(cursor.getColumnIndex(START_TIME)));
            lecture.setEndTime(cursor.getLong(cursor.getColumnIndex(END_TIME)));
            lecture.setVenue(cursor.getString(cursor.getColumnIndex(VENUE)));
            
            String s = cursor.getString(cursor.getColumnIndex(STATUS));
            if(s != null)
                lecture.setStatus(Status.valueOf(s));
            
            CoursesHelper courseshelper = new CoursesHelper(context);
            lecture.setCourse(courseshelper.getCourse(cursor.getInt(cursor.getColumnIndex(COURSE_ID))));
            int s2 = cursor.getInt(cursor.getColumnIndex(CLASS_REP_ID));
            ClassRepHelper classrephelper = new ClassRepHelper(context);
            lecture.setClassRep(classrephelper.getClassRep(s2));
            lecture.setLecturer(cursor.getString(cursor.getColumnIndex(LECTURER)));
            
            String trigger = cursor.getString(cursor.getColumnIndex(LECTURE_TRIGGER));
            if(trigger != null)
            	lecture.setAlarmTrigger(LectureAlarmTrigger.valueOf(trigger));
            
            classrephelper.disconnect();
            courseshelper.disconnect();
            
            return lecture;
        } 
            return null;
        
    }

    public Cursor getLectureSchedules()
    {
        return db.query(LECTURES, cols, null, null, null, null, START_TIME+ " DESC");
    }

    public Cursor getLectureSchedules(Day day)
    {
    	if(day.equals(Day.CHOOSE))
            return getLectureSchedules();
    	
    	String whereClause = DAY + " = ?";
    	String whereArgs[] = {day.name()};
    	
        return db.query(LECTURES, cols, whereClause, whereArgs, null, null, START_TIME +" DESC");
    }


    public List<Lecture> getPendingLectures()
    {
    	Lecture lecture;
    	List<Lecture> lectures = new ArrayList<Lecture>();
    	
        Cursor cursor =  db.query(MySqliteOpenHelper.Tables.LECTURES, cols, null, null, null, null, null);
        
        if(cursor.moveToFirst()){
        	do{
        		lecture = new Lecture();
        		lecture.setId(cursor.getInt(cursor.getColumnIndex(MySqliteOpenHelper.Lectures._ID)));
        		lecture.setDay(cursor.getString(cursor.getColumnIndex(DAY)));
        		lecture.setEndTime(cursor.getLong(cursor.getColumnIndex(END_TIME)));
        		lecture.setLecturer(cursor.getString(cursor.getColumnIndex(LECTURER)));
        		lecture.setStartTime(cursor.getLong(cursor.getColumnIndex(START_TIME)));
        		lecture.setVenue(cursor.getString(cursor.getColumnIndex(VENUE)));
        		
        		String trigger = cursor.getString(cursor.getColumnIndex(LECTURE_TRIGGER));
        		lecture.setAlarmTrigger(trigger != null ? LectureAlarmTrigger.valueOf(trigger): null);
        		
        		int courseId = cursor.getInt(cursor.getColumnIndex(COURSE_ID));
        		Course course = new CoursesHelper(context).getCourse(courseId);
        		lecture.setCourse(course != null ? course: null);
        		
        		String status = cursor.getString(cursor.getColumnIndex(STATUS));
        		lecture.setStatus(status != null ? Status.valueOf(status) : null);
        		
        		int classRepId = cursor.getInt(cursor.getColumnIndex(CLASS_REP_ID));
        		ClassRep classRep = new ClassRepHelper(context).getClassRep(classRepId);
        		lecture.setClassRep(classRep != null ? classRep: null);
        		
        		lectures.add(lecture);
        		
        	}while(cursor.moveToNext());
        }
        
        return lectures;
    }

    public void update(Lecture lecture)
    {
    	String whereClause = MySqliteOpenHelper.Lectures._ID + " = ? ";
        String whereArgs[] = {Integer.toString(lecture.getId())};
        
        db.update(LECTURES, Mapper.mapToLecture(lecture), whereClause, whereArgs);
        
    }

	public void deleteByCourseId(int courseId) {
		String whereClause = COURSE_ID +" = ?";
		String whereArg[] = {Integer.toString(courseId)};
		
		db.delete(LECTURES, whereClause, whereArg);	
	}

}
