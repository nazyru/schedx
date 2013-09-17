
package com.nazir.schedx.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.nazir.schedx.types.Day;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.*;

@SuppressLint("SimpleDateFormat")
public class DateTimeHelper
{
	private static Calendar cal = Calendar.getInstance();
	
   
    public static long getDateMillis(int i, int j, int k)
    {
        cal.set(1, i);
        cal.set(2, j);
        cal.set(5, k);
        return cal.getTimeInMillis();
    }

    public static long getDateMillis(int year, int month, int day, int hour, int minute)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTimeInMillis();
    }

    public static int getDay(long l)
    {
        cal.setTimeInMillis(l);
        return cal.get(5);
    }

    public static int getHour(long l)
    {
        cal.setTimeInMillis(l);
        return cal.get(11);
    }

    public static int getMinute(long l)
    {
        cal.setTimeInMillis(l);
        return cal.get(12);
    }

    public static int getMonth(long l)
    {
        cal.setTimeInMillis(l);
        return cal.get(2);
    }

    public static String getPrintableTime(long l)
    {
        return "Prototype";
    }

    public static long getTimeMillis(int i, int j)
    {
        cal.set(11, i);
        cal.set(12, j);
        return cal.getTimeInMillis();
    }

    public static long getTimeMillis(Day day, long time)
    {
        cal.setTimeInMillis(time);
        
        switch(day){
		
		case FRIDAY:
			cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			break;
		case MONDAY:
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			break;
		case SATURDAY:
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			break;
		case SUNDAY:
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			break;
		case THURSDAY:
			cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
			break;
		case TUESDAY:
			cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			break;
		case WEDNESDAY:
			cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
			break;
		default:
			break;
        
        }
        
        Calendar now = Calendar.getInstance();
        now.set(HOUR_OF_DAY, cal.get(HOUR_OF_DAY));
        now.set(MINUTE, cal.get(MINUTE));
        now.set(DAY_OF_WEEK, cal.get(DAY_OF_WEEK));
        
        	if(now.getTimeInMillis() < getInstance().getTimeInMillis()){
        		now.add(DAY_OF_WEEK, 7);	//Add One Week
        	}

        return now.getTimeInMillis();

    }

    
	public static String getTimeToString(long millis)
    {
        Date date = new Date(millis);
    	SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        return formatter.format(date);
    }

    public static int getYear(long l)
    {
        cal.setTimeInMillis(l);
        return cal.get(1);
    }

  
	public static long stripSeconds(long l)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(l);
        calendar.set(13, 0);
        return calendar.getTimeInMillis();
    }

	@SuppressLint("SimpleDateFormat")
	public static String getDisplayableDate(long date) {
		SimpleDateFormat formatter = new SimpleDateFormat("EE MMM dd, yyyy hh:mm a ");
		return formatter.format(new Date(date));
	}

	public static CharSequence getDateToString(long l) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
		return formatter.format(new Date(l));
	}

}
