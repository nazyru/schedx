
package com.nazir.schedx.persist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.nazir.schedx.model.Todo;
import com.nazir.schedx.types.AlarmRepeatMode;
import com.nazir.schedx.types.AlarmTrigger;
import com.nazir.schedx.util.DateTimeHelper;
import com.nazir.schedx.util.Mapper;
import java.util.ArrayList;
import java.util.List;

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
    public TodosHelper(Context context1)
    {
        context = context1;
        db = MySqliteOpenHelper.getWritableDb(context);
    }

    public void addMockTodo(int i)
    {
        db.delete("todos", null, null);
        Todo todo = new Todo();
        int j = 1;
        do
        {
            if(j > i)
                return;
            todo.setName((new StringBuilder("I wanna Call my mom ")).append(j).toString());
            todo.setRating(j);
            todo.setDescription("i Wanna Call my mom so that we discuss about my next vacation");
            todo.setTime(DateTimeHelper.getTimeMillis(j, j + 5));
            db.insert("todos", null, Mapper.mapTodos(todo));
            j++;
        } while(true);
    }

    public long addTodo(Todo todo)
    {
        return db.insertOrThrow("todos", null, Mapper.mapTodos(todo));
    }

    public void delete(int i)
    {
        SQLiteDatabase sqlitedatabase = db;
        String as[] = new String[1];
        as[0] = Integer.toString(i);
        sqlitedatabase.delete("todos", "_id = ?", as);
    }

    public void disconnect()
    {
        db.close();
    }

    public List<Todo> getPendingTodos()
    {
        ArrayList<Todo> arraylist = new ArrayList<Todo>();
        Cursor cursor = db.query("todos", cols, null, null, null, null, null);
        if(cursor.moveToFirst())
        {
            nameIndx = cursor.getColumnIndex("name");
            descIndx = cursor.getColumnIndex("description");
            timeIndx = cursor.getColumnIndex("TIME");
            ratingIndx = cursor.getColumnIndex("rating");
            repeatModeindx = cursor.getColumnIndex("alarm_repeat_mode");
            triggerIndx = cursor.getColumnIndex("alarm_trigger");
            idIndx = cursor.getColumnIndex("_id");
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
            return arraylist;
        } else
        {
            return null;
        }
    }

    public Todo getTodo(int i)
    {
        String as[] = new String[1];
        as[0] = Integer.toString(i);
        Cursor cursor = db.query("todos", cols, "_id = ?", as, null, null, null);
        Todo todo = new Todo();
        if(cursor.moveToFirst())
        {
            todo.setID(i);
            todo.setName(cursor.getString(cursor.getColumnIndex("name")));
            todo.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            todo.setTime(cursor.getLong(cursor.getColumnIndex("TIME")));
            todo.setRating(cursor.getFloat(cursor.getColumnIndex("rating")));
            String s = cursor.getString(cursor.getColumnIndex("alarm_trigger"));
            AlarmTrigger alarmtrigger;
            String s1;
            AlarmRepeatMode alarmrepeatmode;
            if(s != null)
                alarmtrigger = AlarmTrigger.valueOf(s);
            else
                alarmtrigger = null;
            todo.setAlarmTrigger(alarmtrigger);
            s1 = cursor.getString(cursor.getColumnIndex("alarm_repeat_mode"));
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
        return db.query("todos", cols, null, null, null, null, "rating DESC");
    }

    public void updateTodo(Todo todo)
    {
        String s = Integer.toString(todo.getID());
        db.update("todos", Mapper.mapTodos(todo), "_id = ? ", new String[] {
            s
        });
    }

    String cols[] = {
        "_id", "name", "description", "TIME", "rating", "alarm_trigger", "alarm_repeat_mode"
    };
    
}
