

package com.nazir.schedx.persist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.Date;

public class MySqliteOpenHelper extends SQLiteOpenHelper

{
    public static String DB_NAME = "schedX.db";
    private static int VERSION_NUMBER = 22;

    public interface Assessment
    {
        String DATE = "date";
        String ID = "_id";
        String LOCATION = "location";
        String MEMO = "memo";
        String TRIGGER_MODE = "assessment_trigger_mode";
        String TYPE = "assessment_type";
        String COURSE_ID = "course_id";
    }

    public interface ClassRep
    {
        String EMAIL_ADDRESS = "email_address";
        String NAME = "name";
        String PHONE_NUMBER = "phone_number";
        String REG_NO = "reg_number";
        String _ID = "_id";
    }

    public interface Courses
    {
        String COURSE_CODE = "course_code";
        String COURSE_TITLE = "course_title";
        String COURSE_UNIT = "course_unit";
        String _ID = "_id";
    }

    public interface Lectures
    {
        String DAY = "day";
        String END_TIME = "end_time";
        String LECTURER = "lecturer";
        String START_TIME = "start_time";
        String STATUS = "status";
        String VENUE = "venue";
        String _ID = "_id";
        String LECTURE_TRIGGER = "lecture_trigger"; 
        String CLASS_REP_ID = "class_rep_id";
        String COURSE_ID = "course_id";
    }

    public static interface Tables
    {

        String ASSESSMENT = "assessment";
        String CLASS_REP = "class_rep";
        String COURSES = "courses";
        String LECTURES = "lectures";
        String TODOS = "todos";
    }

    public static interface Todos
    {
        String DESCRIPTION = "description";
        String ID = "_id";
        String NAME = "name";
        String RATING = "rating";
        String REPEAT_MODE = "alarm_repeat_mode";
        String TIME = "TIME";
        String TRIGGER = "alarm_trigger";
    }


    public MySqliteOpenHelper(Context context)
    {
        super(context, DB_NAME, null, VERSION_NUMBER);
    }

    public static SQLiteDatabase getWritableDb(Context context)
    {
        return (new MySqliteOpenHelper(context)).getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + Tables.COURSES +"(" 
        		+ Courses._ID +" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " 
        		+ Courses.COURSE_CODE + " TEXT NOT NULL, " 
        		+ Courses.COURSE_TITLE + " TEXT NOT NULL, " 
        		+ Courses.COURSE_UNIT +" INTEGER )");
        
        db.execSQL("CREATE TABLE " + Tables.LECTURES + " ( " 
        		+ Lectures._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " 
        		+ Lectures.COURSE_ID + " INTEGER NOT NULL, " 
        		+ Lectures.DAY + " TEXT NOT NULL, " 
        		+ Lectures.LECTURER + " TEXT, " 
        		+ Lectures.START_TIME +" INTEGER NOT NULL, " 
        		+ Lectures.END_TIME + " INTEGER, " 
        		+ Lectures.VENUE + " TEXT, " 
        		+ Lectures.STATUS + " TEXT, " 
        		+ Lectures.CLASS_REP_ID + " INTEGER, " 
        		+ Lectures.LECTURE_TRIGGER + " TEXT)");
        
        db.execSQL("CREATE TABLE " + Tables.CLASS_REP + " (" 
        		+ ClassRep._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," 
        		+ ClassRep.REG_NO + " TEXT NOT NULL ," 
        		+ ClassRep.NAME + " TEXT NOT NULL," 
        		+ ClassRep.PHONE_NUMBER + " TEXT, " 
        		+ ClassRep.EMAIL_ADDRESS + " TEXT )");
        
        db.execSQL("CREATE TABLE " + Tables.TODOS +"(" 
        		+ Todos.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," 
        		+ Todos.NAME + " TEXT, " 
        		+ Todos.DESCRIPTION + " TEXT, " 
        		+ Todos.TIME + " INTEGER, " 
        		+ Todos.RATING + " REAL, " 
        		+ Todos.TRIGGER + " TEXT, " 
        		+ Todos.REPEAT_MODE + " TEXT )");
        
        db.execSQL("CREATE TABLE " + Tables.ASSESSMENT +"(" 
        		+ Assessment.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " 
        		+ Assessment.COURSE_ID + " INTEGER NOT NULL," 
        		+ Assessment.TYPE + " TEXT, " 
        		+ Assessment.DATE + " INTEGER, " 
        		+ Assessment.LOCATION + " TEXT, " 
        		+ Assessment.MEMO + " TEXT, " 
        		+ Assessment.TRIGGER_MODE + " TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int i, int j)
    {
        Log.w("~SchedX Database Upgrade~", ("Upgrading From "+ " "+ i + " To "+ j+ " On "
        		+ new Date().toString()));
        
        db.execSQL("DROP TABLE IF EXISTS lectures");
        db.execSQL("DROP TABLE IF EXISTS class_rep");
        db.execSQL("DROP TABLE IF EXISTS todos");
        db.execSQL("DROP TABLE IF EXISTS assessment");
        db.execSQL("DROP TABLE IF EXISTS courses");
        
        onCreate(db);
    }

}
