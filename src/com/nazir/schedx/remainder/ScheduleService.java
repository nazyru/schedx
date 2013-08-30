package com.nazir.schedx.remainder;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.nazir.schedx.R;
import com.nazir.schedx.model.*;
import com.nazir.schedx.persist.*;
import com.nazir.schedx.ui.*;
import java.util.Calendar;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Lectures.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Todos.*;

public class ScheduleService extends IntentService
{
	private NotificationManager notifManager;
    private Notification notification;

    public ScheduleService()
    {
        super("MyScheduleService");
    }
    
    protected void onHandleIntent(Intent intent)
    {
        notifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        
        String s = intent.getAction();
        if(s.equals(LectureActivity.LECTURE_ACTION))
        {
            notifyLecture(intent);
        } else
        {
            if(s.equals(TodoActivity.TODO_ACTION))
            {
                notifyTodo(intent);
                return;
            }
            if(s.equals(AssessmentActivity.ASSESSMENT_ACTION))
            {
                notifyAssessment(intent);
                return;
            }
        }
    }

    private void notifyAssessment(Intent intent)
    {
        int id = intent.getIntExtra(AssessmentActivity.ASSESSMENT_ID, -1);
        Assessment assessment = (new AssessmentHelper(this)).getAssessment(id);
        if(assessment == null)
        	return;
        
        Calendar calendar = Calendar.getInstance();
        long l = assessment.getDate();
        
        if(calendar.getTimeInMillis() > l)
        {
            return;
        } else
        {
            Intent intent1 = new Intent(this, AssessmentDetailActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.putExtra(MySqliteOpenHelper.Assessment.ID, id);
            intent1.putExtra(AssessmentDetailActivity.ASSESSMENT_NOTIF_FLAG, true);
            
            PendingIntent pendingintent = PendingIntent.getActivity(this, assessment.getId(),
            		intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            notification = (new NotificationCompat.Builder(this))
            		.setSmallIcon(R.drawable.notification)
            		.setTicker("Remainder about "+ assessment.getAssessmentType().toString())
            		.setAutoCancel(true)
            		.setContentIntent(pendingintent)
            		.setContentTitle("A remainder about an Assessment")
            		.setContentText(assessment.getCourse().getCourseCode() + " "+assessment.getAssessmentType().toString() +" Notification")
            		.setWhen(assessment.getDate())
            		.build();
            		
            notification.defaults = Notification.DEFAULT_ALL;
            notifManager.notify(AssessmentActivity.ASSESSMENT_ID, id, notification);
            return;
        }
    }

    private void notifyLecture(Intent intent)
    {
        int id = intent.getIntExtra(LectureActivity.LECTURE_ID, -1);
        LecturesHelper lectureshelper = new LecturesHelper(this);
        Lecture lecture = lectureshelper.getLectureSchedlule(id);
        if(lecture == null)
        	return;
        
        Intent intent1 = new Intent(this, LectureDetailActivity.class);
        intent1.putExtra(_ID, id);
        intent1.putExtra(LectureDetailActivity.LECTURE_NOTIF_FLAG, true);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        PendingIntent pendingintent = PendingIntent.getActivity(this, id, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        notification = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.notification)
        .setContentTitle("Lecture Remainder")
        .setContentText(lecture.getCourse().getCourseCode() + " now @ "+ lecture.getVenue())
        .setTicker(lecture.getCourse().getCourseCode() + " now @ "+ lecture.getVenue())
        .setContentIntent(pendingintent)
        .setAutoCancel(true)
        .build();
        
        notification.defaults = Notification.DEFAULT_ALL;
        notifManager.notify(LectureActivity.LECTURE_ID, id, notification);
        lectureshelper.disconnect();
    }

    private void notifyTodo(Intent intent)
    {
        int id = intent.getIntExtra(TodoActivity.TODO_ID, -1);
        TodosHelper todoshelper = new TodosHelper(this);
        Calendar now =  Calendar.getInstance();
        Todo todo = todoshelper.getTodo(id);
        if(todo == null)
        	return;
        
        if(now.getTimeInMillis() - todo.getTime() > 1000)
        {
        	Log.w("--Alarm Passed", "Jenny with " + (now.getTimeInMillis() - todo.getTime()));
            return;
        } else
        {
            Intent intent1 = new Intent(this, TodoDetailActivity.class);
            intent1.putExtra(ID, id);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            
            PendingIntent pendingintent = PendingIntent.getActivity(this, id, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            notification = new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.notification)
            .setContentIntent(pendingintent)
            .setAutoCancel(true)
            .setContentTitle("Task Remainder")
            .setContentText("is time for "+ todo.getName())
            .setTicker(todo.getDescription())
            .setWhen(todo.getTime())
            .build();
            
            notification.defaults = Notification.DEFAULT_ALL;
            notifManager.notify(TodoActivity.TODO_ID, id, notification);
            todoshelper.disconnect();
            return;
        }
    }

}
