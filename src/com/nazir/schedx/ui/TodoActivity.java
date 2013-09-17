
package com.nazir.schedx.ui;

import java.util.Calendar;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;
import com.nazir.schedx.model.Todo;
import com.nazir.schedx.persist.TodosHelper;
import com.nazir.schedx.remainder.AlarmHelper;
import com.nazir.schedx.types.AlarmRepeatMode;
import com.nazir.schedx.types.AlarmTrigger;
import com.nazir.schedx.util.DateTimeHelper;
import com.nazir.schedx.util.MyDatePickerDialog;
import com.nazir.schedx.util.MyTimePickerDialog;
import com.nazir.schedx.util.MyDatePickerDialog.OnDateChangeCallback;
import com.nazir.schedx.util.MyTimePickerDialog.OnTimeChangedCallback;

import static com.nazir.schedx.persist.MySqliteOpenHelper.Todos.*;


public class TodoActivity extends MyCustomActivity implements OnDateChangeCallback, 
  OnTimeChangedCallback
{
    public static String TODO_ACTION = "com.nazir.schedx.ui.TODO_ACTION";
    public static String TODO_ID = "com.nazir.schedx.ui.TODO_ID";
    private Spinner repeatModeList;
    private ArrayAdapter<AlarmRepeatMode> repeatmodeAdapter;
    private ArrayAdapter<AlarmTrigger> triggerAdapter;
    private Spinner triggerList;
    private EditText nameView;
    private EditText descView;
    private TextView datePicker;
    private TextView timePicker;
    private RatingBar ratingBar;
    private Calendar dateTimeCal = Calendar.getInstance();

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.todo_layout);
        
        nameView = (EditText)findViewById(R.id.todo_name_view);
        descView = (EditText)findViewById(R.id.todo_desc_view);
        
        datePicker = (TextView)findViewById(R.id.todo_date_picker);
        timePicker = (TextView)findViewById(R.id.todo_time_picker);
        datePicker.setOnClickListener(new MyClickHandler());
        timePicker.setOnClickListener(new MyClickHandler());
        
        ratingBar = (RatingBar)findViewById(R.id.todo_rating_bar);
        
        triggerList = (Spinner)findViewById(R.id.alarm_trigger_spinner);
        repeatModeList = (Spinner)findViewById(R.id.alarm_repeat_mode_spinner);
        triggerAdapter = new ArrayAdapter<AlarmTrigger>(this, android.R.layout.simple_list_item_1,
        		AlarmTrigger.values());
        repeatmodeAdapter = new ArrayAdapter<AlarmRepeatMode>(this, android.R.layout.simple_list_item_1,
        		AlarmRepeatMode.values());
        triggerList.setAdapter(triggerAdapter);
        repeatModeList.setAdapter(repeatmodeAdapter);
        
        if(getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_EDIT))
            doEdit();
    }

    private void doEdit()
    {
        Bundle bundle = getIntent().getBundleExtra("com.nazir.schedx.ui.todo");
        
        TodosHelper todoshelper = new TodosHelper(this);
        Todo todo = todoshelper.getTodo(bundle.getInt(ID));
        todoshelper.disconnect();
        
        long time = todo.getTime();
        dateTimeCal.setTimeInMillis(time);
        nameView.setText(todo.getName());
        descView.setText(todo.getDescription());
       
        timePicker.setText(DateTimeHelper.getTimeToString(dateTimeCal.getTimeInMillis()));
        datePicker.setText(DateTimeHelper.getDateToString(dateTimeCal.getTimeInMillis()));
        
        ratingBar.setRating(todo.getRating());
        triggerList.setSelection(triggerAdapter.getPosition(todo.getAlarmTrigger()));
        repeatModeList.setSelection(repeatmodeAdapter.getPosition(todo.getAlarmRepeatMode()));
    }

    private void doSave()
    {
        Todo todo;
        TodosHelper todoshelper;
     
        String name = nameView.getText().toString().trim();
        String desc = descView.getText().toString();
        
        todo = new Todo(name, dateTimeCal.getTimeInMillis());
        todo.setDescription(desc);
        todo.setRating(ratingBar.getRating());
        AlarmTrigger alarmtrigger = (AlarmTrigger)triggerAdapter.getItem(triggerList.getSelectedItemPosition());
        AlarmRepeatMode alarmrepeatmode = (AlarmRepeatMode)repeatmodeAdapter.getItem(repeatModeList.getSelectedItemPosition());
        todo.setAlarmTrigger(alarmtrigger);
        todo.setAlarmRepeatMode(alarmrepeatmode);
        todoshelper = new TodosHelper(this);
        String action = getIntent().getAction();
        
        try{
        	if(action != null && action.equals(Intent.ACTION_EDIT)){
        		todo.setID(getIntent().getBundleExtra(TodoListActivity.TODO_BUNDLE).getInt(ID));
                todoshelper.updateTodo(todo);
                AlarmHelper.updateTodoRemainder(todo, this);
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        	}
        	else{
        		todo.setID((int)todoshelper.addTodo(todo));
                AlarmHelper.updateTodoRemainder(todo, this);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        	}
        }catch(SQLException sqlExc){
        	Toast.makeText(this, "Not Save", Toast.LENGTH_SHORT).show();
        }
        finally{
        	todoshelper.disconnect();
            finish();
        }

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
        default: return super.onOptionsItemSelected(menuitem);
        }
        return true;
    }

	@Override
	public void onTimeChanged(DialogFragment dialog, Calendar date) {
		dateTimeCal.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
		dateTimeCal.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
		
		timePicker.setText(DateTimeHelper.getTimeToString(dateTimeCal.getTimeInMillis()));
		
	}

	@Override
	public void onDateChanged(Calendar newDate) {
		dateTimeCal.set(Calendar.DAY_OF_MONTH, newDate.get(Calendar.DAY_OF_MONTH));
		dateTimeCal.set(Calendar.MONTH, newDate.get(Calendar.MONTH));
		dateTimeCal.set(Calendar.YEAR, newDate.get(Calendar.YEAR));

		datePicker.setText(DateTimeHelper.getDateToString(dateTimeCal.getTimeInMillis()));
		
	}
	
	private class MyClickHandler implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.todo_date_picker:
				showDatePicker();
				break;
			case R.id.todo_time_picker:
				showTimePicker();
				break;
			default:
				break;
			}
			
		}

		private void showTimePicker() {
		
			DialogFragment fragment = new MyTimePickerDialog();
			Bundle bundle = new Bundle();
			bundle.putLong(MyTimePickerDialog.INIT_TIME_BUNDLE, dateTimeCal.getTimeInMillis());
			fragment.setArguments(bundle);
			fragment.show(getSupportFragmentManager(), "TodoTimePicker");
		}

		private void showDatePicker() {
			DialogFragment fragment = new MyDatePickerDialog();
			Bundle bundle = new Bundle();
			bundle.putLong(MyDatePickerDialog.INIT_DATE_BUNDLE, dateTimeCal.getTimeInMillis());
			fragment.setArguments(bundle);
			fragment.show(getSupportFragmentManager(), "TodoDatePicker");
		}
	}
}
