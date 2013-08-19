package com.nazir.schedx.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.*;
import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;
import com.nazir.schedx.model.Course;
import com.nazir.schedx.model.Lecture;
import com.nazir.schedx.persist.CoursesHelper;
import com.nazir.schedx.persist.LecturesHelper;
import com.nazir.schedx.persist.MySqliteOpenHelper;
import com.nazir.schedx.remainder.AlarmHelper;
import com.nazir.schedx.types.Day;
import com.nazir.schedx.types.LectureAlarmTrigger;
import com.nazir.schedx.util.DateTimeHelper;
import com.nazir.schedx.util.PreferenceHelper;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Lectures.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Courses.COURSE_CODE;

public class LectureActivity extends MyCustomActivity
{
    public static String LECTURE_ACTION = "com.nazir.schedx.ui.LECTURE_ACTION";
    public static String LECTURE_ID = "com.nazir.schedx.ui.LECTURE_ID";
    private Bundle bundle;
    private Cursor cursor;
    private Spinner daySpinner;
    private ArrayAdapter<Day> daysAdapter;
    private TimePicker endTimePicker;
    private EditText lecturerEditView;
    private TimePicker startTimePicker;
    private EditText venue;
    private Spinner lectureTriggerList;
    private ArrayAdapter<LectureAlarmTrigger> lectureTriggerAdapter;

    public void onCreate(Bundle bundle1)
    {
        super.onCreate(bundle1);
        setContentView(R.layout.lecture_layout);
        
        if((new PreferenceHelper(this)).isStudentMode())
        {
            lecturerEditView = (EditText)findViewById(R.id.lecturer_edit_view);
            lecturerEditView.setVisibility(EditText.VISIBLE);
        }
        startTimePicker = (TimePicker)findViewById(R.id.start_time_picker);
        endTimePicker = (TimePicker)findViewById(R.id.end_time_picker);
        venue = (EditText)findViewById(R.id.lecture_venue_view);
        daysAdapter = new ArrayAdapter<Day>(this, android.R.layout.simple_list_item_1, Day.values());
        daySpinner = (Spinner)findViewById(R.id.lecture_day_spinner);
        lectureTriggerList = (Spinner) findViewById(R.id.lecture_trigger_spinner);
        lectureTriggerAdapter = new ArrayAdapter<LectureAlarmTrigger>(this, android.R.layout.simple_list_item_1, LectureAlarmTrigger.values());
        lectureTriggerList.setAdapter(lectureTriggerAdapter);
        daySpinner.setAdapter(daysAdapter);
        Spinner spinner = (Spinner)findViewById(R.id.courses_spinner);
        cursor = (new CoursesHelper(this)).getCourses();
        spinner.setAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor,
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
        int j = startTimePicker.getCurrentHour();
        int k = startTimePicker.getCurrentMinute();
        int l = endTimePicker.getCurrentHour();
        int i1 = endTimePicker.getCurrentMinute();
        lecture = new Lecture(course, day.name(), DateTimeHelper.getTimeMillis(j, k));
        lecture.setEndTime(DateTimeHelper.getTimeMillis(l, i1));
        lecture.setVenue(venue.getText().toString().trim());
        lecture.setAlarmTrigger(lectureTriggerAdapter.getItem(lectureTriggerList
        		.getSelectedItemPosition()));
        
        if(lecturerEditView != null)
            lecture.setLecturer(lecturerEditView.getText().toString().trim());
        
        lectureshelper = new LecturesHelper(this);
        
        String action = getIntent().getAction();
        
        try{
        if(action != null && action.equals(Intent.ACTION_EDIT)){ 
        	lecture.setId(bundle.getInt(_ID));
        	lectureshelper.update(lecture);
        	Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
        else{
        	int id = (int)lectureshelper.addLecture(lecture);
        	lecture.setId(id);
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

    private void populateViews()
    {
        int i = bundle.getInt(_ID);
        LecturesHelper lectureshelper = new LecturesHelper(this);
        Lecture lecture = lectureshelper.getLectureSchedlule(i);
        lectureshelper.disconnect();
        long startTime = lecture.getStartTime();
        long endTime = lecture.getEndTime();
        startTimePicker.setCurrentHour(DateTimeHelper.getHour(startTime));
        startTimePicker.setCurrentMinute(DateTimeHelper.getMinute(startTime));
        endTimePicker.setCurrentHour(DateTimeHelper.getHour(endTime));
        endTimePicker.setCurrentMinute(DateTimeHelper.getMinute(endTime));
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

}
