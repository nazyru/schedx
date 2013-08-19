
package com.nazir.schedx.ui;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Editable;
import android.widget.*;
import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;
import com.nazir.schedx.model.Todo;
import com.nazir.schedx.persist.TodosHelper;
import com.nazir.schedx.remainder.AlarmHelper;
import com.nazir.schedx.types.AlarmRepeatMode;
import com.nazir.schedx.types.AlarmTrigger;
import com.nazir.schedx.util.DateTimeHelper;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Todos.*;


public class TodoActivity extends MyCustomActivity
{
    public static String TODO_ACTION = "com.nazir.schedx.ui.TODO_ACTION";
    public static String TODO_ID = "com.nazir.schedx.ui.TODO_ID";
    private Spinner repeatModeList;
    private ArrayAdapter<AlarmRepeatMode> repeatmodeAdapter;
    private ArrayAdapter<AlarmTrigger> triggerAdapter;
    private Spinner triggerList;

    private void doEdit()
    {
        Bundle bundle = getIntent().getBundleExtra("com.nazir.schedx.ui.todo");
        EditText edittext = (EditText)findViewById(R.id.todo_name_view);
        EditText edittext1 = (EditText)findViewById(R.id.todo_desc_view);
        DatePicker datepicker = (DatePicker)findViewById(R.id.todo_date_picker);
        TimePicker timepicker = (TimePicker)findViewById(R.id.todo_time_picker);
        RatingBar ratingbar = (RatingBar)findViewById(R.id.todo_rating_bar);
        TodosHelper todoshelper = new TodosHelper(this);
        Todo todo = todoshelper.getTodo(bundle.getInt(ID));
        todoshelper.disconnect();
        long time = todo.getTime();
        edittext.setText(todo.getName());
        edittext1.setText(todo.getDescription());
        timepicker.setCurrentHour(DateTimeHelper.getHour(time));
        timepicker.setCurrentMinute(DateTimeHelper.getMinute(time));
        ratingbar.setRating(todo.getRating());
        datepicker.updateDate(DateTimeHelper.getYear(time), DateTimeHelper.getMonth(time), DateTimeHelper.getDay(time));
        triggerList.setSelection(triggerAdapter.getPosition(todo.getAlarmTrigger()));
        repeatModeList.setSelection(repeatmodeAdapter.getPosition(todo.getAlarmRepeatMode()));
    }

    private void doSave()
    {
        Todo todo;
        TodosHelper todoshelper;
        
        EditText nameView = (EditText)findViewById(R.id.todo_name_view);
        EditText descView = (EditText)findViewById(R.id.todo_desc_view);
        DatePicker datepicker = (DatePicker)findViewById(R.id.todo_date_picker);
        TimePicker timepicker = (TimePicker)findViewById(R.id.todo_time_picker);
        RatingBar ratingbar = (RatingBar)findViewById(R.id.todo_rating_bar);
        String name = nameView.getText().toString().trim();
        String desc = descView.getText().toString();
        int year = datepicker.getYear();
        int month = datepicker.getMonth();
        int day = datepicker.getDayOfMonth();
        int hour = timepicker.getCurrentHour();
        int minute = timepicker.getCurrentMinute();
        
        todo = new Todo(name, DateTimeHelper.getDateMillis(year, month, day, hour, minute));
        todo.setDescription(desc);
        todo.setRating(ratingbar.getRating());
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

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.todo_layout);
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

}
