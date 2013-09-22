package com.nazir.schedx.ui;

import java.util.Calendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.widget.TimePicker.OnTimeChangedListener;

import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;
import com.nazir.schedx.model.Course;
import com.nazir.schedx.model.Lecture;
import com.nazir.schedx.persist.CoursesHelper;
import com.nazir.schedx.persist.LecturesHelper;
import com.nazir.schedx.persist.MySqliteOpenHelper;
import com.nazir.schedx.remainder.AlarmHelper;
import com.nazir.schedx.types.Day;
import com.nazir.schedx.types.DialogFlag;
import com.nazir.schedx.types.LectureAlarmTrigger;
import com.nazir.schedx.types.Status;
import com.nazir.schedx.util.DateTimeHelper;
import com.nazir.schedx.util.MyDatePickerDialog;
import com.nazir.schedx.util.MyTimePickerDialog;
import com.nazir.schedx.util.MyDatePickerDialog.OnDateChangeCallback;
import com.nazir.schedx.util.MyTimePickerDialog.OnTimeChangedCallback;
import com.nazir.schedx.util.PreferenceHelper;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Lectures.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Courses.COURSE_CODE;

public class LectureActivity extends MyCustomActivity implements OnTimeChangedCallback
{
    public static String LECTURE_ACTION = "com.nazir.schedx.ui.LECTURE_ACTION";
    public static String LECTURE_ID = "com.nazir.schedx.ui.LECTURE_ID";
    public static String FLAG = "com.nazir.schedx.ui.START_FINISH_FLAG";
    private Bundle bundle;
    private Cursor cursor;
    private Spinner daySpinner;
    private Spinner courseSpinner;
    private ArrayAdapter<Day> daysAdapter;
    private TextView endTimePicker;
    private EditText lecturerEditView;
    private TextView startTimePicker;
    private EditText venue;
    private Spinner lectureTriggerList;
    private ArrayAdapter<LectureAlarmTrigger> lectureTriggerAdapter;
    private long startTime;
    private long endTime;

    @SuppressWarnings("deprecation")
	public void onCreate(Bundle bundle1)
    {
        super.onCreate(bundle1);
        setContentView(R.layout.lecture_layout);
        
        venue = (EditText)findViewById(R.id.lecture_venue_view);
        
        if((new PreferenceHelper(this)).isStudentMode())
        {
            lecturerEditView = (EditText)findViewById(R.id.lecturer_edit_view);
            lecturerEditView.setVisibility(EditText.VISIBLE);
            venue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }
        
        startTimePicker = (TextView)findViewById(R.id.start_time_picker);
        endTimePicker = (TextView)findViewById(R.id.end_time_picker);
        startTimePicker.setOnClickListener(new MyClickhandler());
        endTimePicker.setOnClickListener(new MyClickhandler());
        startTime = endTime = Calendar.getInstance().getTimeInMillis();
        
        daysAdapter = new ArrayAdapter<Day>(this, android.R.layout.simple_list_item_1, Day.values());
        daySpinner = (Spinner)findViewById(R.id.lecture_day_spinner);
        lectureTriggerList = (Spinner) findViewById(R.id.lecture_trigger_spinner);
        lectureTriggerAdapter = new ArrayAdapter<LectureAlarmTrigger>(this, android.R.layout.simple_list_item_1, LectureAlarmTrigger.values());
        lectureTriggerList.setAdapter(lectureTriggerAdapter);
        daySpinner.setAdapter(daysAdapter);
        courseSpinner = (Spinner)findViewById(R.id.courses_spinner);
        cursor = (new CoursesHelper(this)).getCourses();
        courseSpinner.setAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor,
        		new String[] {COURSE_CODE}, new int[] {android.R.id.text1}));
        
        String action = getIntent().getAction();
        if(action != null && action.equals(Intent.ACTION_EDIT))
        {
            bundle = getIntent().getBundleExtra(LectureListActivity.LECTURE_BUNDLE);
            populateViews();
        }
        
    }

    private void doSave()
    {
        Lecture lecture;
        LecturesHelper lectureshelper;
        int i = cursor.getColumnIndex(MySqliteOpenHelper.Courses._ID);
        
        int courseId = cursor.getInt(i);
        Course course = new CoursesHelper(this).getCourse(courseId);
        Day day = (Day)daySpinner.getSelectedItem();
        
        lecture = new Lecture(course, day.name(), startTime);
        lecture.setEndTime(endTime);
        lecture.setVenue(venue.getText().toString().trim());
        lecture.setAlarmTrigger(lectureTriggerAdapter.getItem(lectureTriggerList
        		.getSelectedItemPosition()));
        
        if(lecturerEditView != null)
            lecture.setLecturer(lecturerEditView.getText().toString().trim());
        
        lectureshelper = new LecturesHelper(this);
        
        String action = getIntent().getAction();
        
        if(!isValid(lecture))
        	return;
        
        try{
        if(action != null && action.equals(Intent.ACTION_EDIT)){ 
        	lecture.setId(bundle.getInt(_ID));
        	lecture.setStatus(Status.valueOf(bundle.getString(STATUS)));
        	lectureshelper.update(lecture);
        	Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
        else{
        	int id = (int)lectureshelper.addLecture(lecture);
        	lecture.setId(id);	//I am setting the returned ID here because AlarmHelper needs ID
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            
        }
        }catch(SQLException exp){
        	Toast.makeText(this, "Unable to Save", Toast.LENGTH_SHORT).show();
        }
        finally{
        	AlarmHelper.updateLectureRemainder(lecture, this);
        	lectureshelper.disconnect();
            finish();
        }
        
    }
    
    private boolean isValid(Lecture lecture){
    	boolean valid = true;
    	
    	if(lecture.getVenue().equals("")){
    		venue.setError("Venue Can't be Empty");
    		valid = false;
    	}
    	return valid;
    }

    private void populateViews()
    {
        int i = bundle.getInt(_ID);
        LecturesHelper lectureshelper = new LecturesHelper(this);
        Lecture lecture = lectureshelper.getLectureSchedlule(i);
        lectureshelper.disconnect();
        startTime = lecture.getStartTime();
        endTime = lecture.getEndTime();
        
        startTimePicker.setText(DateTimeHelper.getTimeToString(startTime));
        endTimePicker.setText(DateTimeHelper.getTimeToString(endTime));
        
        venue.setText(lecture.getVenue());
        
        if(lecturerEditView != null)
            lecturerEditView.setText(lecture.getLecturer());
        
        Day day = Day.valueOf(lecture.getDay());
        daySpinner.setSelection(daysAdapter.getPosition(day), true);
        
        LectureAlarmTrigger alarmTrigger = lecture.getAlarmTrigger();
        lectureTriggerList.setSelection(lectureTriggerAdapter.getPosition(alarmTrigger), true);
        
        
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.action_bar_lecture, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId()){
        case R.id.ic_save_lecture:
        	 doSave();
        	 break;
        case android.R.id.home:
        	Intent intent = new Intent(this, MainActivity.class);
        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(intent);
        	break;
        default:  return super.onOptionsItemSelected(menuitem);
        }
        
        return true;
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == EditorInfo.IME_ACTION_DONE){
			doSave();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onTimeChanged(DialogFragment dialog, Calendar date) {
		int flag = dialog.getArguments().getInt(FLAG);
		
		switch(flag){
		case 1:
			startTime = date.getTimeInMillis();
			startTimePicker.setText(DateTimeHelper.getTimeToString(startTime));
			break;
		case 2:
			endTime = date.getTimeInMillis();
			endTimePicker.setText(DateTimeHelper.getTimeToString(endTime));
			break;
		default:
			break;
		}
	}
	
	private class MyClickhandler implements OnClickListener{

		@Override
		public void onClick(View v) {
			int flag;
			long initTime;
			
			if((TextView)v == startTimePicker){
				flag = 1;
				initTime = startTime;
			}
			else{
				flag=2;
				initTime = endTime;
			}
			
			DialogFragment dialog = new MyTimePickerDialog();
			Bundle bundle = new Bundle();
			bundle.putInt(FLAG, flag);
			bundle.putLong(MyTimePickerDialog.INIT_TIME_BUNDLE, initTime);
			dialog.setArguments(bundle);
			dialog.show(getSupportFragmentManager(), "TimePickerDialog");
		}
		
	}
}
