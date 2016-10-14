
package com.nazir.schedx.model;

import com.nazir.schedx.types.AlarmRepeatMode;
import com.nazir.schedx.types.AlarmTrigger;

public class Todo
{
    private int ID;
    private AlarmRepeatMode alarmRepeatMode;
    private AlarmTrigger alarmTrigger;
    private String description;
    private String name;
    private float rating;
    private long time;

    public Todo()
    {
    }

    public Todo(String s, long l)
    {
        name = s;
        time = l;
    }

    public boolean equals(Object obj)
    {
        if(this != obj)
        {
            if(obj == null)
                return false;
            if(getClass() != obj.getClass())
                return false;
            Todo todo = (Todo)obj;
            if(ID != todo.ID)
                return false;
        }
        return true;
    }

    public AlarmRepeatMode getAlarmRepeatMode()
    {
        return alarmRepeatMode;
    }

    public AlarmTrigger getAlarmTrigger()
    {
        return alarmTrigger;
    }

    public String getDescription()
    {
        return description;
    }

    public int getID()
    {
        return ID;
    }

    public String getName()
    {
        return name;
    }

    public float getRating()
    {
        return rating;
    }

    public long getTime()
    {
        return time;
    }

    public int hashCode()
    {
        return 31 + ID;
    }

    public void setAlarmRepeatMode(AlarmRepeatMode alarmrepeatmode)
    {
        alarmRepeatMode = alarmrepeatmode;
    }

    public void setAlarmTrigger(AlarmTrigger alarmtrigger)
    {
        alarmTrigger = alarmtrigger;
    }

    public void setDescription(String s)
    {
        description = s;
    }

    public void setID(int i)
    {
        ID = i;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setRating(float f)
    {
        rating = f;
    }

    public void setTime(long l)
    {
        time = l;
    }

    public String toString()
    {
        return name;
    }

}
