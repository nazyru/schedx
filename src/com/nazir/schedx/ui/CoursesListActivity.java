package com.nazir.schedx.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;
import com.nazir.schedx.model.Course;
import com.nazir.schedx.persist.CoursesHelper;
import com.nazir.schedx.types.CourseFlag;

import static com.nazir.schedx.persist.MySqliteOpenHelper.Courses.*;

public class CoursesListActivity extends MyCustomListActivity
{
    private Cursor cursor;
    private ActionMode mActionMode;
    private ActionMode.Callback mCallback;
    private View dialogView;
    private EditText courseCodeView;
    private EditText courseTitleView;
    private EditText courseUnitView;
    private SimpleCursorAdapter adapter;
    
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        initActionMode();
        
        dialogView = getLayoutInflater().inflate(R.layout.add_course_layout, null, false);
        courseCodeView = (EditText)dialogView.findViewById(R.id.course_code_widget);
        courseTitleView = (EditText)dialogView.findViewById(R.id.course_title_view);
        courseUnitView = (EditText)dialogView.findViewById(R.id.course_unit_view);
        
        String as[] = {COURSE_CODE, COURSE_TITLE};
        int ai[] = {android.R.id.text1, android.R.id.text2};
       
        cursor = (new CoursesHelper(this)).getCourses();
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, as, ai);
   
        setListAdapter(adapter);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> adapterview, View view, int i, long l)
            {
                if(mActionMode != null)
                {
                    return false;
                } else
                {
                    mActionMode = startActionMode(mCallback);
                    return true;
                }
            }
        });
    }

    private void initActionMode()
    {
        mCallback = new ActionMode.Callback() {

            public boolean onActionItemClicked(ActionMode actionmode, MenuItem menuitem)
            {
                switch(menuitem.getItemId()){
                case R.id.edit_schedule_action_item:
                	doAddCourse(CourseFlag.EDIT);
                    actionmode.finish();
                    break;
                case R.id.action_delete_item:
                	doDelete();
                    actionmode.finish();
                    break;
                default:  return false;
                }
          return true;
       }

            public boolean onCreateActionMode(ActionMode actionmode, Menu menu)
            {
                actionmode.getMenuInflater().inflate(R.menu.context_menu, menu);
                return true;
            }

            public void onDestroyActionMode(ActionMode actionmode)
            {
                mActionMode = null;
            }

            public boolean onPrepareActionMode(ActionMode actionmode, Menu menu)
            {
                return false;
            }
            
        };

    }

    protected void doDelete()
    {
    	new AlertDialog.Builder(this)
    	.setMessage(R.string.delete_course_message)
    	.setTitle(R.string.delete_course_title)
    	.setNegativeButton(R.string.delete_course_no_button, new AlertDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		})
		.setPositiveButton(R.string.delete_course_yes_button, new AlertDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				int id = cursor.getInt(cursor.getColumnIndex(_ID));
				doDeleteSchedules(id);
			}
		})
		.show();
        
    }

    protected void doDeleteSchedules(int id) {
		CoursesHelper helper = new CoursesHelper(this);
		helper.delete(id);
		Toast.makeText(CoursesListActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
		
		cursor = helper.getCourses();
		adapter.changeCursor(cursor);
		helper.disconnect();
	}

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId()){
        case R.id.action_item_new:
        	doAddCourse(CourseFlag.NEW);
        	break;
        case android.R.id.home:
        	Intent intent = new Intent(this, MainActivity.class);
        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(intent);
        	break;
        default: return super.onOptionsItemSelected(menuitem);
        	
        }
        return true;
    }

	private void doAddCourse(final CourseFlag flag) {
		
		String positiveButtonLabel = flag == CourseFlag.NEW ? "Add" : "Update";
		
		dialogView = getLayoutInflater().inflate(R.layout.add_course_layout, null, false);
        courseCodeView = (EditText)dialogView.findViewById(R.id.course_code_widget);
        courseTitleView = (EditText)dialogView.findViewById(R.id.course_title_view);
        courseUnitView = (EditText)dialogView.findViewById(R.id.course_unit_view);
        
        if(flag == CourseFlag.EDIT){
        	int id = cursor.getInt(cursor.getColumnIndex(_ID));
        	CoursesHelper helper = new CoursesHelper(this);
    		Course course = helper.getCourse(id);
    		if(course == null)
    			return;
    		
    		courseCodeView.setText(course.getCourseCode());
    		courseTitleView.setText(course.getCourseTitle());
    		courseUnitView.setText(Integer.toString(course.getCourseUnit()));
        }
        
        View view = getLayoutInflater().inflate(R.layout.add_course_title_layout, null, false);
        TextView viewHeader = (TextView) view.findViewById(R.id.add_course_title_layout);
        viewHeader.setText(flag == CourseFlag.NEW ? "Add Course" : "Update Course");
        
		AlertDialog alertDialog = new AlertDialog.Builder(this)
		.setView(dialogView)
		.setCustomTitle(view)
		.setPositiveButton(positiveButtonLabel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				doSave(flag);
				dialog.dismiss();
			}
		})
		.setNegativeButton(R.string.add_course_cancel_button_hint, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		})
		.create();
		
		alertDialog.show();
	}
	
	private void doSave(CourseFlag flag)
    {
        Course course = new Course();
        
        if(courseCodeView.getText().toString() != null)
            course.setCourseCode(courseCodeView.getText().toString().trim());
        
        if(courseTitleView.getText().toString() != null)
            course.setCourseTitle(courseTitleView.getText().toString().trim());
        
        
        if(courseUnitView.getText().toString() != null)
            course.setCourseUnit(Integer.parseInt(courseUnitView.getText().toString().trim()));
        
        if(!isValid(course))
        	return;
        
        CoursesHelper coursesHelper = new CoursesHelper(this);
        
        try{
        if(flag == CourseFlag.EDIT){
        	
        	int id = cursor.getInt(cursor.getColumnIndex(_ID));
        	course.setID(id);
            coursesHelper.updateCourse(course);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
        else{
        	coursesHelper.addCourse(course);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        }catch(SQLException sqlExc){
        	 Log.i("Courses", sqlExc.getMessage());
             Toast.makeText(this, "Not Saved", Toast.LENGTH_SHORT).show();
        }
        finally{
        	cursor = coursesHelper.getCourses();
        	adapter.notifyDataSetChanged();
        	adapter.changeCursor(cursor);
        	coursesHelper.terminate();
        }
  
    }
	private boolean isValid(Course course)
    {
		Log.i("--HERE--", course.getCourseCode() + course.getCourseTitle()+ course.getCourseUnit());
        boolean flag = true;
        
        if(course.getCourseCode().equals(""))
        {
            Toast.makeText(this, "Not Saved: Course Code can't be Empty", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if(course.getCourseTitle().equals(""))
        {
            Toast.makeText(this, "Not Saved: Title can't be Empty", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }
}
