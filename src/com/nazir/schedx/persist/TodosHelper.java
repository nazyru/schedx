
package com.nazir.schedx.persist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.nazir.schedx.model.Todo;
import com.nazir.schedx.types.AlarmRepeatMode;
import com.nazir.schedx.types.AlarmTrigger;
import com.nazir.schedx.util.Mapper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Todos.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Tables.*;

public class TodosHelper
{
    private Context context;
    private SQLiteDatabase db;
    int descIndx;
    int idIndx;
    int nameIndx;
    int ratingIndx;
    int repeatModeindx;
    int timeIndx;
    int triggerIndx;
    String cols[] = {
            ID, NAME, DESCRIPTION, TIME, RATING, TRIGGER, REPEAT_MODE
        };
    
    public TodosHelper(Context context1)
    {
        context = context1;
        db = MySqliteOpenHelper.getWritableDb(context);
    }
    
    public long addTodo(Todo todo)
    {
        return db.insertOrThrow(TODOS, null, Mapper.mapTodos(todo));
    }

    public void delete(int i)
    {
        SQLiteDatabase sqlitedatabase = db;
        String as[] = new String[1];
        as[0] = Integer.toString(i);
        sqlitedatabase.delete(TODOS, "_id = ?", as);
    }

    public void disconnect()
    {
        db.close();
    }

    public List<Todo> getPendingTodos()
    {
        ArrayList<Todo> arraylist = new ArrayList<Todo>();
        Cursor cursor = db.query(TODOS, cols, null, null, null, null, null);
        if(cursor.moveToFirst())
        {
            nameIndx = cursor.getColumnIndex(NAME);
            descIndx = cursor.getColumnIndex(DESCRIPTION);
            timeIndx = cursor.getColumnIndex(TIME);
            ratingIndx = cursor.getColumnIndex(RATING);
            repeatModeindx = cursor.getColumnIndex(REPEAT_MODE);
            triggerIndx = cursor.getColumnIndex(TRIGGER);
            idIndx = cursor.getColumnIndex(ID);
            do
            {
                Todo todo = new Todo();
                todo.setID(cursor.getInt(idIndx));
                todo.setName(cursor.getString(nameIndx));
                todo.setDescription(cursor.getString(descIndx));
                todo.setTime(cursor.getLong(timeIndx));
                todo.setRating(cursor.getFloat(ratingIndx));
                String s = cursor.getString(repeatModeindx);
                String s1 = cursor.getString(triggerIndx);
                AlarmRepeatMode alarmrepeatmode;
                AlarmTrigger alarmtrigger;
                if(s != null)
                    alarmrepeatmode = AlarmRepeatMode.valueOf(s);
                else
                    alarmrepeatmode = null;
                todo.setAlarmRepeatMode(alarmrepeatmode);
                if(s1 != null)
                    alarmtrigger = AlarmTrigger.valueOf(s1);
                else
                    alarmtrigger = null;
                todo.setAlarmTrigger(alarmtrigger);
                arraylist.add(todo);
            } while(cursor.moveToNext());
            
        } 
        return arraylist;
    }

    public Todo getTodo(int i)
    {
        String as[] = new String[1];
        as[0] = Integer.toString(i);
        Cursor cursor = db.query(TODOS, cols, "_id = ?", as, null, null, null);
        Todo todo = new Todo();
        if(cursor.moveToFirst())
        {
            todo.setID(i);
            todo.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            todo.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
            todo.setTime(cursor.getLong(cursor.getColumnIndex(TIME)));
            todo.setRating(cursor.getFloat(cursor.getColumnIndex(RATING)));
            String s = cursor.getString(cursor.getColumnIndex(TRIGGER));
            AlarmTrigger alarmtrigger;
            String s1;
            AlarmRepeatMode alarmrepeatmode;
            if(s != null)
                alarmtrigger = AlarmTrigger.valueOf(s);
            else
                alarmtrigger = null;
            todo.setAlarmTrigger(alarmtrigger);
            s1 = cursor.getString(cursor.getColumnIndex(REPEAT_MODE));
            alarmrepeatmode = null;
            if(s1 != null)
                alarmrepeatmode = AlarmRepeatMode.valueOf(s1);
            todo.setAlarmRepeatMode(alarmrepeatmode);
            return todo;
        } else
        {
            return null;
        }
    }

    public Cursor getTodos()
    {
    	Calendar now = Calendar.getInstance();
    	long time = now.getTimeInMillis();
    	String selection = TIME + " > ?";
    	String selectionArgs[] = {Long.toString(time)};
    	String orderBy = TIME+" DESC";
        return db.query(TODOS, cols, selection, selectionArgs, null, null, orderBy);
    }

    public void updateTodo(Todo todo)
    {
        String s = Integer.toString(todo.getID());
        db.update(TODOS, Mapper.mapTodos(todo), "_id = ? ", new String[] {
            s
        });
    }

    
    
	public Cursor getDoneTasks() {
		Calendar now = Calendar.getInstance();
		long time = now.getTimeInMillis();
		String selection = TIME + " < ?";
		String[] selectionArgs = {Long.toString(time)};
		return db.query(TODOS, cols, selection, selectionArgs, null, null, TIME + " DESC");
	}
    
}
