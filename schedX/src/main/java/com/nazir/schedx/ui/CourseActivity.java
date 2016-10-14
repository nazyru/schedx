package com.nazir.schedx.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.*;
import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;
import com.nazir.schedx.model.Course;
import com.nazir.schedx.persist.CoursesHelper;

public class CourseActivity extends MyCustomActivity
{

    public CourseActivity()
    {
    }

    private void doSave()
    {
        Course course;
        course = new Course();
        EditText edittext = (EditText)findViewById(R.id.course_code_widget);
        EditText edittext1 = (EditText)findViewById(R.id.course_title_view);
        EditText edittext2 = (EditText)findViewById(R.id.course_unit_view);
        if(edittext.getText().toString() != null)
            course.setCourseCode(edittext.getText().toString().trim());
        if(edittext1.getText().toString() != null)
            course.setCourseTitle(edittext1.getText().toString().trim());
        if(edittext2.getText().toString() != null)
            course.setCourseUnit(Integer.parseInt(edittext2.getText().toString().trim()));
        if(!isValid(course))
        	return;

        String action = getIntent().getAction();
        CoursesHelper courseshelper1 = new CoursesHelper(this);
        
        try{
        if(action != null  && action.equals(Intent.ACTION_EDIT)){
        	//set ID here
            courseshelper1.updateCourse(course);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
        else{
        	courseshelper1.addCourse(course);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        }catch(SQLiteException sqlExc){
        	 Log.i("Courses", sqlExc.getMessage());
             Toast.makeText(this, "Not Saved", Toast.LENGTH_SHORT).show();
        }
        finally{
        	courseshelper1.terminate();
            finish();
        }
  
    }

    private boolean isValid(Course course)
    {
        boolean flag = true;
        if(course.getCourseCode() == null)
        {
            Toast.makeText(this, "Not Saved: Course Code can't be Empty", 1).show();
            flag = false;
        }
        if(course.getCourseTitle() == null)
        {
            Toast.makeText(this, "Not Saved: Title can't be Empty", 1).show();
            flag = false;
        }
        return flag;
    }

    private void populateFields()
    {
        
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.add_course_layout);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String s = getIntent().getAction();
        if(s != null && s.equals(Intent.ACTION_EDIT))
            populateFields();
        
        ((EditText)findViewById(R.id.course_unit_view)).setOnEditorActionListener(
        		new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView textview, int i, KeyEvent keyevent)
            {
                if(i == 2)
                    doSave();
                return true;
            }

        }
);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.action_bar_lecture, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId())
        {
        case R.id.ic_save_lecture: 
            doSave();
            break;
        default:
            return super.onOptionsItemSelected(menuitem);
        }
        return true;
    }

}
