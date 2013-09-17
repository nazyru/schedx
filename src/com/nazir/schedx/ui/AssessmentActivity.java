
package com.nazir.schedx.ui;

import java.util.Calendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.actionbarsherlock.view.*;
import com.itextpdf.text.Document;
import com.nazir.schedx.R;
import com.nazir.schedx.model.Assessment;
import com.nazir.schedx.model.Course;
import com.nazir.schedx.persist.AssessmentHelper;
import com.nazir.schedx.persist.CoursesHelper;
import com.nazir.schedx.remainder.AlarmHelper;
import com.nazir.schedx.types.AssessmentTriggerMode;
import com.nazir.schedx.types.AssessmentType;
import com.nazir.schedx.util.DateTimeHelper;
import com.nazir.schedx.util.MyDatePickerDialog;
import com.nazir.schedx.util.MyDatePickerDialog.OnDateChangeCallback;
import com.nazir.schedx.util.MyTimePickerDialog;
import com.nazir.schedx.util.MyTimePickerDialog.OnTimeChangedCallback;

import static com.nazir.schedx.persist.MySqliteOpenHelper.Assessment.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Courses.*;

public class AssessmentActivity extends MyCustomActivity implements OnDateChangeCallback,
	OnTimeChangedCallback
{
    public static String ASSESSMENT_ACTION = "com.nazir.schedx.ui.ASSESSMENT_ACTION_STRING";
    public static String ASSESSMENT_ID = "com.nazir.schedx.ui.ASSESSMENT_ID_STRING";
    private TextView assessDatePicker;
    private TextView assessTimePicker;
    private Spinner assessTypeSpinner;
    private ArrayAdapter<AssessmentTriggerMode> assessmentTriggerAdapter;
    private Spinner assessmentTriggerModeSpinner;
    private ArrayAdapter<AssessmentType> assessmentTypeAdapter;
    private Spinner coursesSpinner;
    private Cursor cursor;
    private EditText memoTextView;
    private EditText venueTextView;
    private int id;
    private Calendar dateTimeCal = Calendar.getInstance();
    
    @SuppressWarnings("deprecation")
	public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.assessment_layout);
        assessmentTypeAdapter = new ArrayAdapter<AssessmentType>(this, android.R.layout.simple_list_item_1,
        		AssessmentType.values());
        coursesSpinner = (Spinner)findViewById(R.id.assessment_course_spinner);
        assessTypeSpinner = (Spinner)findViewById(R.id.assessment_type_spinner);
        assessDatePicker = (TextView)findViewById(R.id.assessment_date_picker);
        assessTimePicker = (TextView)findViewById(R.id.assessment_time_picker);
     
        assessDatePicker.setOnClickListener(new MyClickHandler());
        assessTimePicker.setOnClickListener(new MyClickHandler());
        
        venueTextView = (EditText)findViewById(R.id.assessment_venue_view);
        memoTextView = (EditText)findViewById(R.id.assessment_memo_view);
        assessmentTriggerModeSpinner = (Spinner)findViewById(R.id.assessment_trigger_mode);
        assessmentTriggerAdapter = new ArrayAdapter<AssessmentTriggerMode>(this, android.R.layout.simple_list_item_1,
        		AssessmentTriggerMode.values());
        assessmentTriggerModeSpinner.setAdapter(assessmentTriggerAdapter);
        
        CoursesHelper courseshelper = new CoursesHelper(this);
        cursor = courseshelper.getCourses();
        
		SimpleCursorAdapter coursesAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, 
        		cursor, new String[] {COURSE_CODE}, new int[] {android.R.id.text1});
        
        assessTypeSpinner.setAdapter(assessmentTypeAdapter);
        coursesSpinner.setAdapter(coursesAdapter);
        if(getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_EDIT))
            populateControls();
    }
    
    private void doSave()
    {
        Assessment assessment = new Assessment();
        AssessmentHelper assessmenthelper = new AssessmentHelper(this);
        
        int courseId = cursor.getInt(cursor.getColumnIndex(_ID));
        Course course = new CoursesHelper(this).getCourse(courseId);
        assessment.setCourse(course);
     
        assessment.setAssessmentType((AssessmentType)assessTypeSpinner.getSelectedItem());
        assessment.setDate(dateTimeCal.getTimeInMillis());
        assessment.setLocation(venueTextView.getText().toString().trim());
        
        assessment.setMemo(memoTextView.getText().toString().trim());
        assessment.setTriggerMode((AssessmentTriggerMode)assessmentTriggerAdapter
        			.getItem(assessmentTriggerModeSpinner.getSelectedItemPosition()));
       
        try{
        	if(getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_EDIT)){
        		
        		id = getIntent().getBundleExtra(AssessmentListActivity.ASSESSMENT_BUNDLE)
        				.getInt(ID);
        		
        		assessment.setId(id);
        		assessmenthelper.update(assessment);
        		Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        	}
        	else{
        		id = (int) assessmenthelper.addAssessment(assessment);
        		assessment.setId(id);
        		Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        	}
        }catch(SQLException sqlExc){
        	Log.w("Assessment", sqlExc.getMessage());
        	Toast.makeText(this, "Not Save!", Toast.LENGTH_SHORT).show();
        }
        finally{
        	AlarmHelper.updateAssessmentRemainder(assessment, this);
        	assessmenthelper.terminate();
        	finish();
        }
    }

    private void populateControls()
    {
        int i = getIntent().getBundleExtra(AssessmentListActivity.ASSESSMENT_BUNDLE).getInt("_id", -1);
        AssessmentHelper assessmenthelper = new AssessmentHelper(this);
        Assessment assessment = assessmenthelper.getAssessment(i);
        assessmenthelper.disconnect();
        
        long l = assessment.getDate();
        dateTimeCal.setTimeInMillis(l);
        assessTimePicker.setText(DateTimeHelper.getTimeToString(l));
        assessDatePicker.setText(DateTimeHelper.getDateToString(l));
        
        venueTextView.setText(assessment.getLocation());
        memoTextView.setText(assessment.getMemo());
        assessmentTriggerModeSpinner.setSelection(assessmentTriggerAdapter.getPosition(assessment.getTriggerMode()), true);
        assessTypeSpinner.setSelection(assessmentTypeAdapter.getPosition(assessment.getAssessmentType()), true);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
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

	@Override
	public void onTimeChanged(DialogFragment dialog, Calendar date) {
		dateTimeCal.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
		dateTimeCal.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
		
		assessTimePicker.setText(DateTimeHelper.getTimeToString(dateTimeCal.getTimeInMillis()));
		
	}

	@Override
	public void onDateChanged(Calendar newDate) {
		dateTimeCal.set(Calendar.DAY_OF_MONTH, newDate.get(Calendar.DAY_OF_MONTH));
		dateTimeCal.set(Calendar.MONTH, newDate.get(Calendar.MONTH));
		dateTimeCal.set(Calendar.YEAR, newDate.get(Calendar.YEAR));
		
		assessDatePicker.setText(DateTimeHelper.getDateToString(dateTimeCal.getTimeInMillis()));
		
	}
	
	private class MyClickHandler implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.assessment_date_picker:
				showDatePicker();
				break;
			case R.id.assessment_time_picker:
				showTimePicker();
				break;
			default:
				break;
			}
		}

		private void showTimePicker() {
			
			DialogFragment dialog = new MyTimePickerDialog();
			Bundle bundle = new Bundle();
			bundle.putLong(MyTimePickerDialog.INIT_TIME_BUNDLE, dateTimeCal.getTimeInMillis());
			dialog.setArguments(bundle);
			dialog.show(getSupportFragmentManager(), "AssessmrntTimePicker");
		}

		private void showDatePicker() {
			DialogFragment fragment = new MyDatePickerDialog();
			Bundle bundle = new Bundle();
			bundle.putLong(MyDatePickerDialog.INIT_DATE_BUNDLE, dateTimeCal.getTimeInMillis());
			fragment.setArguments(bundle);
			fragment.show(getSupportFragmentManager(), "MyDatePicker");
			
			
		}
	}
}
