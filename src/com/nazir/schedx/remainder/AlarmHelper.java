
package com.nazir.schedx.remainder;

import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nazir.schedx.model.Assessment;
import com.nazir.schedx.model.Lecture;
import com.nazir.schedx.model.Todo;
import com.nazir.schedx.persist.LecturesHelper;
import com.nazir.schedx.types.Day;
import com.nazir.schedx.ui.AssessmentActivity;
import com.nazir.schedx.ui.LectureActivity;
import com.nazir.schedx.ui.TodoActivity;
import com.nazir.schedx.util.DateTimeHelper;

public class AlarmHelper
{

    public static void updateAssessmentRemainder(Assessment assessment, Context context)
    {
        AlarmManager alarmmanager;
        PendingIntent pendingintent;
        long triggerAtMillis = assessment.getDate();
        long dayMillis = 24 * 60 * 60 * 1000;
        alarmmanager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ScheduleReceiver.class);
        intent.setAction(AssessmentActivity.ASSESSMENT_ACTION + assessment.getId());
        intent.putExtra(AssessmentActivity.ASSESSMENT_ID, assessment.getId());
        pendingintent = PendingIntent.getBroadcast(context, assessment.getId(), intent, 
        		PendingIntent.FLAG_CANCEL_CURRENT);
        
        switch(assessment.getTriggerMode()){
		case FIFTEEN:
			triggerAtMillis -= 15 * dayMillis;
			break;
		case FIVE:
			 triggerAtMillis -= 5 * dayMillis;
			break;
		case OFF:
			alarmmanager.cancel(pendingintent);
			return;
		case TEN:
			triggerAtMillis -= 10 * dayMillis;
			break;
		case TWENTY:
			triggerAtMillis -= 20 * dayMillis;
			break;
		case TWO:
			triggerAtMillis -= 2 * dayMillis;
			break;
		default:
			break;
        
        }
        alarmmanager.set(AlarmManager.RTC_WAKEUP, DateTimeHelper.stripSeconds(triggerAtMillis),
        		pendingintent);
        
    }

    public static void updateTodoRemainder(Todo todo, Context context)
    {	
    	long minuteMillis = 60 * 1000;	
        long hourMillis = 60 * minuteMillis;
        long repeatAtMillis = -1;
        long triggerAtMillis = todo.getTime();
        AlarmManager alarmManager;
        PendingIntent pendingIntent;
        
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ScheduleReceiver.class);
        intent.setAction(TodoActivity.TODO_ACTION + todo.getID());
        intent.putExtra(TodoActivity.TODO_ID, todo.getID());
        pendingIntent = PendingIntent.getBroadcast(context, todo.getID(), intent, 
        		PendingIntent.FLAG_CANCEL_CURRENT);
   
        switch(todo.getAlarmTrigger()){
		case DAY:
			triggerAtMillis -= 24 * hourMillis;
			break;
		case FIVE:
			triggerAtMillis -= 5 * minuteMillis;
			break;
		case HOUR:
			triggerAtMillis -= hourMillis;
			break;
		case OFF:
			alarmManager.cancel(pendingIntent);
			return;
		case TEN:
			 triggerAtMillis -= 10 * minuteMillis;
			break;
		case THIRTY:
			triggerAtMillis -= 30 * minuteMillis;
			break;
		default:
			break;
        }
        
        switch(todo.getAlarmRepeatMode()){
		case ONCE_A_DAY:
			repeatAtMillis = 24 * hourMillis;
			break;
		case ONCE_A_MONTH:
			 repeatAtMillis = 30 * 24 * hourMillis;
			break;
		case ONCE_A_WEEK:
			repeatAtMillis = 7 * 24 *  hourMillis;
			break;
		default: break;
          }
        
        if(repeatAtMillis == -1){
        	
        	alarmManager.set(AlarmManager.RTC_WAKEUP, DateTimeHelper.stripSeconds(triggerAtMillis),
        			pendingIntent);
        	return;
        }
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, DateTimeHelper.stripSeconds(triggerAtMillis),
        		 repeatAtMillis, pendingIntent);   
    }
    
    public static void updateLectureRemainder(Lecture lecture, Context context)
    {
    	int id = lecture.getId();
        Intent intent = new Intent(context, ScheduleReceiver.class);
        intent.setAction(LectureActivity.LECTURE_ACTION);
        intent.putExtra(LectureActivity.LECTURE_ID, id);
        long repeatAtMillis = 7 * 24 * 60 * 60 * 1000; 
        long minuteMillis = 60 * 1000;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
        PendingIntent pendingintent = PendingIntent.getBroadcast(context, id, intent, 
        		PendingIntent.FLAG_CANCEL_CURRENT);
        
        long triggerAtMillis = DateTimeHelper.getTimeMillis(Day.valueOf(lecture.getDay()), 
        		lecture.getStartTime());
        
        switch(lecture.getAlarmTrigger()){
		case FIFTEEN:
			triggerAtMillis -= 15 * minuteMillis;
			break;
		case FIVE:
			triggerAtMillis -= 5 * minuteMillis;
			break;
		case OFF:
			alarmManager.cancel(pendingintent);
			return;
		case TEN:
			triggerAtMillis -= 10 * minuteMillis;
			break;
		case THIRTY:
			triggerAtMillis -= 30 * minuteMillis;
			break;
		case TWENTY:
			triggerAtMillis -= 20 * minuteMillis;
			break;
		default:
			break;
        
        }
        
        Log.i("--TRiggering @ ", new Date(triggerAtMillis).toString());
        
        	alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, DateTimeHelper.stripSeconds(triggerAtMillis),
        			repeatAtMillis, pendingintent);
    }
}
